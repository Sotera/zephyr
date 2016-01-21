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
package org.zephyr.data;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PairTest {

    Pair pair;

    @Before
    public void setup() {
        pair = new Pair("key", "value");
    }

    @Test
    public void testGetKey() {
        assertEquals("key", pair.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value", pair.getValue());
    }

    public void testToString() {
        assertEquals("Pair [key=key, value=value]", pair.toString());
    }

    @Test
    public void testNullItems() {
        Pair nullPair = new Pair(null, null);
        assertEquals("Pair [key=null, value=null]", nullPair.toString());
    }

}
