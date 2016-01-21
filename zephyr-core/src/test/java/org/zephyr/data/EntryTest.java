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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class EntryTest {

    private Entry value;

    @Before
    public void setup() {
        value = new Entry("test.category", "test.value", Lists.newArrayList("test.type"), "AC1&AC2", "primary");
    }

    @Test
    public void testGetCategory() {
        assertEquals("test.category", value.getLabel());
    }

    @Test
    public void testGetValue() {
        assertEquals("test.value", value.getValue());
    }

    @Test
    public void testGetType() {
        assertEquals("test.type", value.getTypes().get(0));
    }

    @Test
    public void testGetVisibility() {
        assertEquals("AC1&AC2", value.getVisibility());
    }

    @Test
    public void testGetMetadata() {
        assertEquals("primary", value.getMetadata());
    }

    @Test
    public void testSetMetadata() {
        value.setMetadata("new.test.metadata");
        assertEquals("new.test.metadata", value.getMetadata());
    }

}
