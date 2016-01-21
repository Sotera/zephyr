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
package org.zephyr.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.zephyr.data.Pair;
import org.zephyr.data.ProcessingResult;

public class XMLParserTest {

    @Test
    public void test1() throws IOException {
        XMLParserFactory factory = new XMLParserFactory("repeating");
        Parser parser = factory.newParser(Thread.currentThread().getContextClassLoader().getResourceAsStream("xml-parser-test1.xml"));
        ProcessingResult<List<Pair>, byte[]> result = null;
        List<List<Pair>> goodResults = new ArrayList<List<Pair>>();
        while ((result = parser.parse()) != null) {
            if (result.wasProcessedSuccessfully()) {
                goodResults.add(result.getProcessedData());
            }
        }
        assertEquals(2, goodResults.size());
        assertEquals(4, goodResults.get(0).size());
        assertEquals(4, goodResults.get(1).size());
        assertEquals("foo.bar.value", goodResults.get(0).get(0).getKey());
        assertEquals("4", goodResults.get(0).get(0).getValue());
        assertEquals("foo.bar.value", goodResults.get(1).get(0).getKey());
        assertEquals("4", goodResults.get(1).get(0).getValue());
        assertEquals("foo.bar.baz.attr1", goodResults.get(0).get(1).getKey());
        assertEquals("1.0", goodResults.get(0).get(1).getValue());
        assertEquals("foo.bar.baz.attr1", goodResults.get(1).get(1).getKey());
        assertEquals("1.0", goodResults.get(1).get(1).getValue());
        assertEquals("foo.bar.baz.attrX", goodResults.get(0).get(2).getKey());
        assertEquals("4.3", goodResults.get(0).get(2).getValue());
        assertEquals("foo.bar.baz.attrX", goodResults.get(1).get(2).getKey());
        assertEquals("4.3", goodResults.get(1).get(2).getValue());
        assertEquals("foo.bar.baz.repeating", goodResults.get(0).get(3).getKey());
        assertEquals("trout", goodResults.get(0).get(3).getValue());
        assertEquals("foo.bar.baz.repeating", goodResults.get(1).get(3).getKey());
        assertEquals("hamster", goodResults.get(1).get(3).getValue());
    }

}
