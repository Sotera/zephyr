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
