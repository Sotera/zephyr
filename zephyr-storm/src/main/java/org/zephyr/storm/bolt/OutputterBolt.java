package org.zephyr.storm.bolt;

import java.util.Map;

import org.zephyr.data.Record;
import org.zephyr.output.Outputter;
import org.zephyr.storm.Constants;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class OutputterBolt extends ZephyrBolt<Outputter> {

    private static final long serialVersionUID = 3144045097909406598L;

    public OutputterBolt(String springConfig, String springEnricherId) {
        super(springConfig, springEnricherId);
    }


    @SuppressWarnings("rawtypes")
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
        try {
            this.component.initialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Record record = (Record) input.getValueByField(Constants.RECORD);
        Values output = new Values(record); // we're outputting this to success no matter what, failure might get it too!
        try {
            component.output(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
        collector.emit(output);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(Constants.RECORD));
    }

    @Override
    public void cleanup() {
        super.cleanup();
        try {
            this.component.flush();
            this.component.close();
        } catch (Exception e) {
            // well, we tried!
        }
    }

}
