package org.zephyr.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class RecordTest {

    private Record record;
    private Entry value1;
    private Entry value2;

    @Before
    public void setup() {
        record = new Record("fake.uuid", "junit.test");
        value1 = new Entry("fake.category", "some.value", Lists.newArrayList("string"), "AC1", "");
        record.add(value1);
        value2 = new Entry("real.category", "exceptionally.excellent.value", Lists.newArrayList("string"), "AC1", "");
        record.add(value2);
    }

    @Test
    public void testRecordEmptyConstructor() {
        Record testRecord = new Record();
        assertTrue(!testRecord.getUuid().isEmpty());
        assertEquals("", testRecord.getFeedName());
        assertEquals(0, testRecord.size());
        assertTrue(!testRecord.iterator().hasNext());
    }

    @Test
    public void testRecordQualifiedConstructor() {
        assertEquals("fake.uuid", record.getUuid());
        assertEquals("junit.test", record.getFeedName());
    }

    @Test
    public void testGetUuid() {
        Record testRecord = new Record();
        assertEquals(36, testRecord.getUuid().length());
    }

    @Test
    public void testGetFeedName() {
        Record testRecord = new Record();
        testRecord.setFeedName("another.test");
        assertEquals("another.test", testRecord.getFeedName());
    }

    @Test
    public void testAddEntry() {
        Entry newValue = new Entry("third.category", "third.value", Lists.newArrayList("binary"), "", "");
        assertEquals(2, record.size());
        record.add(newValue);
        assertEquals(3, record.size());
    }

    @Test
    public void testAddMultipleEntries() {
        Record testRecord = new Record();
        assertEquals(0, testRecord.size());
        testRecord.add(value1, value2);
        assertEquals(2, testRecord.size());
        assertEquals("fake.category", testRecord.get(0).getLabel());
        assertEquals("real.category", testRecord.get(1).getLabel());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullCV() {
        record.add((Entry) null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullEntries() {
        Record testRecord = new Record();
        testRecord.add(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullEntryInMultipleEntries() {
        Record testRecord = new Record();
        testRecord.add(value1, null, value2);
    }

    @Test
    public void testGetSize() {
        assertEquals(2, record.size());
    }

    @Test
    public void testGetByValidIndex() {
        assertEquals("exceptionally.excellent.value", record.get(1).getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetByInvalidIndex() {
        record.get(5);
    }

    @Test
    public void testIteratorIteration() {
        Iterator<Entry> iterator = record.iterator();
        while (iterator.hasNext()) {
            Entry value = iterator.next();
            assertTrue(value != null);
        }
        for (Entry value : record) {
            assertTrue(value != null);
        }
    }

    @Test
    public void testIteratorRemoval() {
        Iterator<Entry> iterator = record.iterator();
        iterator.next();
        iterator.remove();
        assertEquals(1, record.size());
        assertEquals("real.category", record.get(0).getLabel());
    }

}
