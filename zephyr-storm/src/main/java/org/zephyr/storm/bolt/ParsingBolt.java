package org.zephyr.storm.bolt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.zephyr.data.Pair;
import org.zephyr.data.ProcessingResult;
import org.zephyr.parser.Parser;
import org.zephyr.parser.ParserFactory;
import org.zephyr.storm.Constants;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * ParsingBolt subclasses ZephyrBolt and types the component it encapsulates as a type of org.zephyr.parser.ParserFactory
 * <p/>
 * Regardless of what exact ParserFactory implementation defined by our spring configuration file, we will always
 * behave in the manner specified in the execute method.
 */
public class ParsingBolt extends ZephyrBolt<ParserFactory> {

    private static final long serialVersionUID = -3799488550592115449L;

    public ParsingBolt(String springConfig, String springParserId) {
        super(springConfig, springParserId);
    }

    /**
     * To even begin feeding data to our Zephyr Storm topology, we require that our data be specified to the ParsingBolt
     * under the binary field "record".
     */
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        byte[] record = input.getBinaryByField("record");
        if (record != null && record.length > 0) {
            Parser parser = this.component.newParser(new ByteArrayInputStream(record));
            try {
                ProcessingResult<List<Pair>, byte[]> result = parser.parse();
                if (result.wasProcessedSuccessfully()) {
                    collector.emit(new Values(result.getProcessedData()));
                } else {
                    collector.emit(Constants.PARSE_FAILURE, new Values(record));
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * Our default stream is expected to pass out data that has been successfully parsed, while our
     * error stream, Constants.PARSE_FAILURE, attempts to output the original data passed in
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(Constants.PAIRS));
        declarer.declareStream(Constants.PARSE_FAILURE, new Fields(Constants.RAW_DATA));
    }

}
