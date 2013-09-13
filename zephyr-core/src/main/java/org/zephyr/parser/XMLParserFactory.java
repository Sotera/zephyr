package org.zephyr.parser;

import java.io.InputStream;

import javax.xml.stream.FactoryConfigurationError;

/**
 * XMLParserFactory can provide either a depth first, generalized {@link org.zephyr.parser.DepthFirstXMLParser} for streaming
 * purposes, or (eventually) a breadth first, DOM based parser (for instances where "repeating" values appear "before" the global values and a
 * less efficient, less big-data-ish approach must be used to parse the data out).
 */
public class XMLParserFactory implements ParserFactory {

    public static final String DEPTH_FIRST = "depth_first";

    /**
     * The QName we will expect to "repeat" throughout the document, signifying the beginning of a Record
     * If this document has no repeating records (e.g., it is a Record per document), then use the root QName of the XML document.
     */
    private String repeatingQName;
    private String type;

    public XMLParserFactory(String repeatingQName) {
        this.repeatingQName = repeatingQName;
        this.type = DEPTH_FIRST;
    }

    public void setParserType(String type) {
        this.type = type;
    }

    @Override
    public Parser newParser(InputStream inputStream) {
        if (this.type.equals(DEPTH_FIRST)) {
            return new DepthFirstXMLParser(repeatingQName, inputStream);
        } else {
            throw new FactoryConfigurationError("The only parser implementation available is " + DEPTH_FIRST);
        }
    }

}
