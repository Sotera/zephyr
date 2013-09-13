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
