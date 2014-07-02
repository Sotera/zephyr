/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.zephyr.mapreduce.mapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zephyr.data.Pair;
import org.zephyr.data.ProcessingResult;
import org.zephyr.data.Record;
import org.zephyr.enricher.Enricher;
import org.zephyr.mapreduce.ZephyrMRv1Configuration;
import org.zephyr.output.formatter.OutputFormatter;
import org.zephyr.parser.Parser;
import org.zephyr.parser.ParserFactory;
import org.zephyr.preprocessor.Preprocessor;
import org.zephyr.schema.Schema;

public abstract class ZephyrMapper<K, V> extends MapReduceBase implements Mapper<K, V, Text, Text> {

    private static final Logger logger = LoggerFactory.getLogger(ZephyrMapper.class);

    private static Text SUCCESS = new Text("success");
    private static Text PARSE_FAIL = new Text("parsefailure");
    private static Text SCHEMA_FAIL = new Text("schemafailure");

    private Preprocessor preprocessor;
    private ParserFactory parserFactory;
    private Schema schema;
    private OutputFormatter formatter;
    private List<Enricher> enrichers = new ArrayList<Enricher>();

    protected abstract InputStream getInputStreamFromKeyValue(K key, V value);

    @Override
    public void configure(JobConf jobConf) {
        String feedFile = ensureExists("zephyr.feed.xml", jobConf);
        @SuppressWarnings("resource")
        ApplicationContext springContext = new ClassPathXmlApplicationContext(feedFile);
        ZephyrMRv1Configuration config = springContext.getBean("jobConfig", ZephyrMRv1Configuration.class);
        preprocessor = config.getPreprocessor();
        parserFactory = config.getParserFactory();
        schema = config.getSchema();
        formatter = config.getOutputFormatter();
        enrichers = config.getEnrichers();
    }

    private String ensureExists(String property, JobConf jobConf) {
        String value = jobConf.get(property);
        if ("".equals(value)) {
            throw new RuntimeException("Required zephyr property: " + property + " was not provided in the job configuration!");
        }
        return value;
    }

    @Override
    public void map(K key, V value, OutputCollector<Text, Text> collector, Reporter reporter) {
        InputStream inputStream = getInputStreamFromKeyValue(key, value);
        if (preprocessor != null) {
            byte[] result = preprocessor.process(inputStream);
            inputStream = new ByteArrayInputStream(result);
        }
        Parser zephyrParser = this.parserFactory.newParser(inputStream);
        zephyrParser.setSourceElementIdentifier(((FileSplit) reporter.getInputSplit()).getPath().getName());

        Text output = new Text();
        try {
            ProcessingResult<List<Pair>, byte[]> parseResult;
            while ((parseResult = zephyrParser.parse()) != null) {
                if (parseResult.wasProcessedSuccessfully()) {
                    ProcessingResult<Record, List<Pair>> mappedRecord = schema.map(parseResult.getProcessedData());
                    if (mappedRecord.wasProcessedSuccessfully()) {
                        Record record = mappedRecord.getProcessedData();
                        for (Enricher enricher : enrichers) {
                            try {
                                enricher.enrich(record);
                            } catch (RuntimeException e) {
                                // continue on
                            }
                        }
                        output.set(formatter.formatRecord(record));
                        collector.collect(SUCCESS, output);
                    } else {
                        collector.collect(SCHEMA_FAIL, new Text(getPairsAsBytes(mappedRecord.getRawData())));
                        logger.error("An error occurred during mapping the raw data to our schema", mappedRecord.getError());
                    }
                } else {
                    collector.collect(PARSE_FAIL, new Text(parseResult.getRawData()));
                    logger.error("An error occurred during parsing our raw data", parseResult.getError());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /*
     * A quick dirty method for turning a List of pairs into a byte array
     */
    private byte[] getPairsAsBytes(List<Pair> pairs) {
        StringBuilder builder = new StringBuilder();
        for (Pair pair : pairs) {
            builder.append(pair.getKey());
            builder.append(":");
            builder.append(pair.getValue());
            builder.append(",");
        }
        return builder.substring(0, builder.length() - 1).getBytes();
    }

}
