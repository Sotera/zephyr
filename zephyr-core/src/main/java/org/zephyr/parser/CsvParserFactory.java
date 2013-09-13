package org.zephyr.parser;

import java.io.InputStream;
import java.util.List;

public class CsvParserFactory implements ParserFactory {

    public static final char DEFAULT_SEPARATOR = ',';
    public static final char DEFAULT_QUOTE_CHAR = '"';
    public static final char DEFAULT_ESCAPE_CHAR = '\\';

    private char separator = DEFAULT_SEPARATOR;
    private char quoteChar = DEFAULT_QUOTE_CHAR;
    private char escapeChar = DEFAULT_ESCAPE_CHAR;
    private boolean strictQuotes = false;
    private boolean trimWhitespace = true;
    private List<String> headers;

    public CsvParserFactory(List<String> headers) {
        this.headers = headers;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public void setQuoteChar(char quoteChar) {
        this.quoteChar = quoteChar;
    }

    public void setEscapeChar(char escapeChar) {
        this.escapeChar = escapeChar;
    }

    public void setStrictQuotes(boolean strictQuotes) {
        this.strictQuotes = strictQuotes;
    }

    public void setTrimWhitespace(boolean trimWhitespace) {
        this.trimWhitespace = trimWhitespace;
    }

    @Override
    public Parser newParser(InputStream inputStream) {
        return new CsvParser(this.headers, inputStream, this.separator, this.quoteChar, this.escapeChar, this.strictQuotes, this.trimWhitespace);
    }
}
