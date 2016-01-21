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
