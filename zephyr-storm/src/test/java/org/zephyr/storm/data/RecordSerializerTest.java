package org.zephyr.storm.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.zephyr.data.Entry;
import org.zephyr.data.Record;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.common.collect.Lists;

public class RecordSerializerTest {
	
	private Record record;
	private Entry entry1;
	private Entry entry2;
	private Entry entry3;
	private Kryo kryo;
	
	@Before
	public void setup() {
		record = new Record("uuid1", "feedFoo");
		entry1 = new Entry("label-entry1", "34", Lists.newArrayList("integer"), "U&FOUO", "metadata");
		entry2 = new Entry("entry2", "anemailaddress@w3c.org", Lists.newArrayList("email", "string"), "", "");
		entry3 = new Entry("ip", "10.0.0.1", Lists.newArrayList("ip", "ipv4"), "", "source");	
		record.add(entry1, entry2, entry3);
		kryo = new Kryo();
		kryo.register(Record.class, new RecordSerializer());
		kryo.register(Entry.class, new EntrySerializer());
	}
	
	@Test
	public void testSerde1() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Output output = new Output(baos);
		kryo.writeObject(output, record);
		
		byte[] bytes = output.toBytes();
		
		output.close();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		Input input = new Input(bais);
		
		Record testRec = kryo.readObject(input, Record.class);
		
		assertRecordEquals(record, testRec);
		
		input.close();
	}
	
	@Test
	public void testSerde2() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Output output = new Output(baos);
		Record emptyRecord = new Record();
		kryo.writeObject(output, emptyRecord);
		
		byte[] bytes = output.toBytes();
		
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		Input input = new Input(bais);
		
		Record testRec = kryo.readObject(input, Record.class);
		
		assertRecordEquals(emptyRecord, testRec);
	}
	
	public void assertRecordEquals(Record record1, Record record2) {
		assertEquals("UUIDs did not match", record1.getUuid(), record2.getUuid());
		assertEquals("Feed Names did not match", record1.getFeedName(), record2.getFeedName());
		assertEquals("Entries in records did not match in count!", record1.size(), record2.size());
		for (int i = 0; i < record1.size(); i++) {
			assertEquals("Labels did not match!", record1.get(i).getLabel(), record2.get(i).getLabel());
			assertEquals("Values did not match!", record1.get(i).getValue(), record2.get(i).getValue());
			assertEquals("Metadata did not match!", record1.get(i).getMetadata(), record2.get(i).getMetadata());
			assertEquals("Visibility did not match!", record1.get(i).getVisibility(), record2.get(i).getVisibility());
			List<String> types1 = record1.get(i).getTypes();
			List<String> types2 = record2.get(i).getTypes();
			assertEquals("Sizes of the types between the records did not match!", types1.size(), types2.size());
			for (int j = 0; j < types1.size(); j++) {
				assertEquals("Types did not match in location!", types1.get(j), types2.get(j));
			}
		}
	}

}
