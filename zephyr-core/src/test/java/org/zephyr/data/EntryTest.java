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
