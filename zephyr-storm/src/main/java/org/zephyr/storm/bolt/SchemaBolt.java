package org.zephyr.storm.bolt;

import java.util.List;

import org.zephyr.data.Pair;
import org.zephyr.data.ProcessingResult;
import org.zephyr.data.Record;
import org.zephyr.schema.Schema;
import org.zephyr.storm.Constants;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * SchemaBolt subclasses ZephyrBolt and types the component it encapsulates as a type of org.zephyr.schema.Schema
 * <p/>
 * This Schema is defined by another configuration file, which is instantiated by our schema object upon being created in the prepare method
 */
public class SchemaBolt extends ZephyrBolt<Schema> {

    private static final long serialVersionUID = -6451056708177458995L;

    public SchemaBolt(String springConfig, String springSchemaId) {
        super(springConfig, springSchemaId);
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        @SuppressWarnings("unchecked")
        List<Pair> pairs = (List<Pair>) input.getValueByField(Constants.PAIRS);
        if (pairs != null && pairs.size() > 0) {
            ProcessingResult<Record, List<Pair>> result = component.map(pairs);
            if (result.wasProcessedSuccessfully()) {
                collector.emit(new Values(result.getProcessedData()));
            } else {
                collector.emit(Constants.SCHEMA_FAILURE, new Values(result.getRawData()));
            }
        }
    }

    /**
     * Our default stream is expected to pass out data that has been successfully parsed, while our
     * error stream, Constants.SCHEMA_FAILURE, attempts to output the original data passed in
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(Constants.RECORD));
        declarer.declareStream(Constants.SCHEMA_FAILURE, new Fields(Constants.PAIRS));
    }

}
