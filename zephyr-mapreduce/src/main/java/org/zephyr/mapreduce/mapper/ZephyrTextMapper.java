package org.zephyr.mapreduce.mapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class ZephyrTextMapper extends ZephyrMapper<LongWritable, Text> {

    protected InputStream getInputStreamFromKeyValue(LongWritable key, Text value) {
        return new ByteArrayInputStream(value.copyBytes());
    }

}
