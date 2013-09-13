package org.zephyr.storm.bolt;

import org.zephyr.data.Record;
import org.zephyr.enricher.Enricher;
import org.zephyr.storm.Constants;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class EnricherBolt extends ZephyrBolt<Enricher> {

    private static final long serialVersionUID = 3144045097909406598L;

    public EnricherBolt(String springConfig, String springEnricherId) {
        super(springConfig, springEnricherId);
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Record record = (Record) input.getValueByField(Constants.RECORD);
        Values output = new Values(record); // we're outputting this to success no matter what
        try {
            component.enrich(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
        collector.emit(output);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(Constants.RECORD));
    }

}
