package org.zephyr.mapreduce.mapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;

public class ZephyrBytesWritableMapper extends ZephyrMapper<NullWritable, BytesWritable> {

    protected InputStream getInputStreamFromKeyValue(NullWritable key, BytesWritable value) {
        return new ByteArrayInputStream(value.getBytes(), 0, value.getLength());
    }

}
