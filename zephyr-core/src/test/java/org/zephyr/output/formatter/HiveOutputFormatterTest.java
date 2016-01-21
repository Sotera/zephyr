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
