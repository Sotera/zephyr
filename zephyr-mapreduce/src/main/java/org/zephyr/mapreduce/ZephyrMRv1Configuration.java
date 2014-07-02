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
package org.zephyr.mapreduce;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.Mapper;
import org.zephyr.enricher.Enricher;
import org.zephyr.output.formatter.OutputFormatter;
import org.zephyr.parser.ParserFactory;
import org.zephyr.preprocessor.Preprocessor;
import org.zephyr.schema.Schema;

public class ZephyrMRv1Configuration {

    // Zephyr specific configuration elements
    private Preprocessor preprocessor;

    private ParserFactory parserFactory;

    private Schema schema;

    private List<Enricher> enrichers;

    private OutputFormatter outputFormatter;

    // ZephyrDriver configuration elements (map reduce capabilities we pass through)    
    private Mapper<? extends Writable, ? extends Writable, ? extends Writable, ? extends Writable> mapper;

    private InputFormat<? extends Writable, ? extends Writable> inputFormat;

    private String inputPath;

    private String outputPath;

    private Map<String, String> configMap;

    private String jobName;

    public ZephyrMRv1Configuration() {
        enrichers = new LinkedList<Enricher>();
        configMap = new TreeMap<String, String>();
    }

    public Preprocessor getPreprocessor() {
        return preprocessor;
    }

    public void setPreprocessor(Preprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }

    public ParserFactory getParserFactory() {
        return parserFactory;
    }

    public void setParserFactory(ParserFactory parserFactory) {
        this.parserFactory = parserFactory;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public List<Enricher> getEnrichers() {
        return enrichers;
    }

    public void setEnrichers(List<Enricher> enrichers) {
        this.enrichers = enrichers;
    }

    public OutputFormatter getOutputFormatter() {
        return outputFormatter;
    }

    public void setOutputFormatter(OutputFormatter outputFormatter) {
        this.outputFormatter = outputFormatter;
    }

    public Mapper<? extends Writable, ? extends Writable, ? extends Writable, ? extends Writable> getMapper() {
        return mapper;
    }

    public void setMapper(Mapper<? extends Writable, ? extends Writable, ? extends Writable, ? extends Writable> mapper) {
        this.mapper = mapper;
    }

    public InputFormat<? extends Writable, ? extends Writable> getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(InputFormat<? extends Writable, ? extends Writable> inputFormat) {
        this.inputFormat = inputFormat;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public Map<String, String> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
