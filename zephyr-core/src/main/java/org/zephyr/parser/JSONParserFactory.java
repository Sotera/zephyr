package org.zephyr.parser;

import javax.xml.stream.FactoryConfigurationError;
import java.io.InputStream;

public class JSONParserFactory implements ParserFactory {

    public static final String DEPTH_FIRST = "depth_first";

    private String repeatingObjectName;
    private String type;

    public JSONParserFactory(String repeatingObjectName) {
        this.repeatingObjectName = repeatingObjectName;
        this.type = DEPTH_FIRST;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Parser newParser(InputStream inputStream) {
        if (this.type.equals(DEPTH_FIRST)) {
            return new DepthFirstJSONParser(repeatingObjectName, inputStream);
        } else {
            throw new FactoryConfigurationError("The only parser implementation available is " + DEPTH_FIRST);
        }
    }
}
