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
package org.zephyr.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.zephyr.data.Pair;
import org.zephyr.data.ProcessingResult;

import au.com.bytecode.opencsv.CSVReader;

public class CsvParser extends Parser {

    private CSVReader csvReader;
    private List<String> headers;
    private boolean trimWhitespace;
    private char separator;

    public CsvParser(List<String> headers, InputStream inputStream, char separator, char quoteChar, char escapeChar, boolean strictQuotes, boolean trimWhitespace) {
        this.headers = headers;
        this.csvReader = new CSVReader(new InputStreamReader(inputStream), separator, quoteChar, escapeChar, 0, strictQuotes, trimWhitespace);
        this.trimWhitespace = trimWhitespace;
        this.separator = separator;
    }

    @Override
    public ProcessingResult<List<Pair>, byte[]> parse() throws IOException {
        String[] values = this.csvReader.readNext();
        if (values == null)
            return null;
        if (values.length != headers.size()) {
            StringBuilder builder = new StringBuilder();
            for (String value : values) {
                builder.append(value);
                builder.append(separator);
            }
            byte[] approximateRawData = builder.substring(0, builder.length() - 1).getBytes();
            return new ProcessingResult<List<Pair>, byte[]>(approximateRawData, new ParseException("The number of values parsed, " + values.length + ", does not coincide with the number of expected columns, " + headers.size()));
        }
        List<Pair> pairs = new ArrayList<Pair>(values.length);
        for (int i = 0; i < values.length; i++) {
            String cleanedValue = values[i];
            if (this.trimWhitespace) {
                cleanedValue = cleanedValue.trim();
            }
            pairs.add(new Pair(headers.get(i), cleanedValue));
        }

        if (getSourceElementIdentifier() != null) {
            pairs.add(getSourceElementPair());
        }
        return new ProcessingResult<List<Pair>, byte[]>(pairs);

    }


}
