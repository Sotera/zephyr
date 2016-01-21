/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.zephyr.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.RecordReader;


/*
 * This class was pulled from Tom White's "Hadoop: Definitive Guide", 3rd edition.  It is a stop gap until we can write a Twitter InputSplit for our specific input split problem we have for that
 * feed and the files we are receiving.
 */
class WholeFileRecordReader implements RecordReader<NullWritable, BytesWritable> {

    private FileSplit fileSplit;
    private Configuration conf;
    private boolean processed = false;

    public WholeFileRecordReader(FileSplit fileSplit, Configuration conf) throws IOException {
        this.fileSplit = fileSplit;
        this.conf = conf;
    }

    @Override
    public NullWritable createKey() {
        return NullWritable.get();
    }

    @Override
    public BytesWritable createValue() {
        return new BytesWritable();
    }

    @Override
    public long getPos() throws IOException {
        return processed ? fileSplit.getLength() : 0;
    }

    @Override
    public float getProgress() throws IOException {
        return processed ? 1.0f : 0.0f;
    }

    @Override
    public boolean next(NullWritable key, BytesWritable value) throws IOException {
        if (!processed) {
            int size = (int) fileSplit.getLength();
            byte[] contents = new byte[size];
            Path file = fileSplit.getPath();
            FileSystem fs = file.getFileSystem(conf);
            FSDataInputStream in = null;
            try {
                in = fs.open(file);
                // bug fix for the capacity overflow issue we would have if the number of bytes to read in was > 2/3 of Integer.MAX_VALUE
                if (size > (2 * Integer.MAX_VALUE) / 3) {
                    value.set(new byte[]{0x00}, 0, 1); // erasing the data currently in value so that we don't do a full array copy when we call setCapacity
                    value.setCapacity(size);
                }
                IOUtils.readFully(in, contents, 0, contents.length);
                value.set(contents, 0, contents.length);
            } finally {
                IOUtils.closeStream(in);
            }
            processed = true;
            return true;
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        // do nothing
    }
}