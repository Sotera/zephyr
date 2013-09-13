package org.zephyr.output.formatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zephyr.data.Entry;
import org.zephyr.data.Record;

/**
 * The HiveOutputFormatter is a class that maps a given record to the column order we have been
 * given.
 */
public class HiveOutputFormatter implements OutputFormatter {

    private static final char COL_VAL_SEPARATOR = '\t';

    private List<String> orderedHeaders;
    private char fieldSeparator;

    /**
     * @param orderedHeaders A list of ordered headers - these headers must match up with the value returned from
     *                       {@link org.zephyr.data.Entry#getLabel()}.
     */
    public HiveOutputFormatter(List<String> orderedHeaders) {
        this.orderedHeaders = orderedHeaders;
        fieldSeparator = COL_VAL_SEPARATOR;
    }

    /**
     * @param fieldSeparator allows us to use a different field separator character for the hive output
     */
    public void setFieldSeparator(final char fieldSeparator) {
        this.fieldSeparator = fieldSeparator;
    }

    /**
     * Builds a HashMap lookup for all of the entries in a Record, then
     * iterates through the given orderedHeaders as a list of key lookups.
     * <p/>
     * If no Entry is found for a given header, an empty field is written to ensure properly formated TSV format
     *
     * @param Record record the record to format
     */
    @Override
    public byte[] formatRecord(Record record) {
        Map<String, Entry> entries = new HashMap<String, Entry>();
        for (Entry value : record) {
            entries.put(value.getLabel(), value);
        }

        StringBuilder builder = new StringBuilder();
        for (String header : orderedHeaders) {
            Entry value = entries.get(header);
            if (value != null) {
                builder.append(value.getValue().replaceAll(String.valueOf(fieldSeparator), ""));
            }
            builder.append(fieldSeparator);
        }
        return builder.substring(0, builder.length() - 1).getBytes();
    }

}
