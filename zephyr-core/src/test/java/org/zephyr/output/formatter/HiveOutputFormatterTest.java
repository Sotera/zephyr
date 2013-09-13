package org.zephyr.output.formatter;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.zephyr.data.Entry;
import org.zephyr.data.Record;

import com.google.common.collect.Lists;

public class HiveOutputFormatterTest {

    private Record record;
    private OutputFormatter formatter;

    @Before
    public void setup() {
        record = new Record("uuid1", "test-feed");
        record.add(new Entry("cat1", "val\t1", Collections.<String>emptyList(), "", ""), new Entry("cat2", "val2long", Collections.<String>emptyList(), "", ""));
        formatter = new HiveOutputFormatter(Lists.newArrayList("cat2", "cat1"));
    }

    @Test
    public void testOutput() {
        byte[] output = formatter.formatRecord(record);
        byte[] expectedOutput = new String("val2long\tval1").getBytes();
        assertEquals(expectedOutput.length, output.length);
        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i], output[i]);
        }
    }

}
