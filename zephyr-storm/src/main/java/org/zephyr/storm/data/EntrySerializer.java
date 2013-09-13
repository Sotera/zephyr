package org.zephyr.storm.data;

import java.util.List;

import org.zephyr.data.Entry;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class EntrySerializer extends Serializer<Entry> {

    @Override
    public void write(Kryo kryo, Output output, Entry entry) {
        output.writeString(entry.getLabel());
        output.writeString(entry.getValue());
        kryo.writeClassAndObject(output, entry.getTypes());
        output.writeString(entry.getVisibility());
        output.writeString(entry.getMetadata());
    }

    @Override
    public Entry read(Kryo kryo, Input input, @SuppressWarnings("rawtypes") Class type) {
        String label = input.readString();
        String value = input.readString();
        @SuppressWarnings("unchecked")
        List<String> types = (List<String>) kryo.readClassAndObject(input);
        String visibility = input.readString();
        String metadata = input.readString();
        return new Entry(label, value, types, visibility, metadata);
    }

}
