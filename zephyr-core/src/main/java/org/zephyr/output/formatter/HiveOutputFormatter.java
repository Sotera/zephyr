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
