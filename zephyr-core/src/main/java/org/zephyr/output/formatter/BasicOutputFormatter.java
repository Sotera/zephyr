package org.zephyr.output.formatter;

import org.zephyr.data.Entry;
import org.zephyr.data.Record;

/**
 * A raw(ish) look at a record, primarily for debugging purposes.
 */
public class BasicOutputFormatter implements OutputFormatter {

    private static final char NULL_CHAR = '\u0000';

    public BasicOutputFormatter() {

    }

    @Override
    public byte[] formatRecord(Record record) {

        StringBuilder builder = new StringBuilder();
        builder.append("ru");
        builder.append(NULL_CHAR);
        builder.append(record.getUuid());
        builder.append(NULL_CHAR);
        for (Entry value : record) {
            builder.append("vl");
            builder.append(NULL_CHAR);
            builder.append(value.getLabel());
            builder.append(NULL_CHAR);
            builder.append("vval");
            builder.append(NULL_CHAR);
            builder.append(value.getValue());
            builder.append(NULL_CHAR);
            for (String type : value.getTypes()) {
                builder.append("vt");
                builder.append(NULL_CHAR);
                builder.append(type);
                builder.append(NULL_CHAR);
            }
            builder.append(NULL_CHAR);
            builder.append("vvis");
            builder.append(NULL_CHAR);
            builder.append(value.getVisibility());
            builder.append(NULL_CHAR);
            builder.append("vm");
            builder.append(NULL_CHAR);
            builder.append(value.getMetadata());
            builder.append(NULL_CHAR);
        }
        return builder.substring(0, builder.length() - 1).getBytes();
    }

}
