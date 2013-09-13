package org.zephyr.storm.data;

import org.zephyr.data.Entry;
import org.zephyr.data.Record;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class RecordSerializer extends Serializer<Record> {

    @Override
    public void write(Kryo kryo, Output output, Record record) {
        output.writeString(record.getUuid());
        output.writeString(record.getFeedName());
        output.writeInt(record.size(), true);
        for (Entry entry : record) {
            kryo.writeClassAndObject(output, entry);
        }
    }

    @Override
    public Record read(Kryo kryo, Input input, @SuppressWarnings("rawtypes") Class type) {
        String uuid = input.readString();
        String feedName = input.readString();
        Record record = new Record(uuid, feedName);
        int numEntries = input.readInt(true);
        for (int i = 0; i < numEntries; i++) {
            Entry entry = (Entry) kryo.readClassAndObject(input);
            record.add(entry);
        }
        return record;
    }

}
