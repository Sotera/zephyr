package org.zephyr.parser;

import javax.xml.stream.FactoryConfigurationError;
import java.io.InputStream;

/**
 * Created by jostinowsky on 2014-04-08.
 */
public class JSONParserFactory implements ParserFactory {

    public static final String DEPTH_FIRST = "depth_first";

    private String repeatingQName;
    private String type;

    public JSONParserFactory(String repeatingQName) {
        this.repeatingQName = repeatingQName;
        this.type = DEPTH_FIRST;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Parser newParser(InputStream inputStream) {
        if (this.type.equals(DEPTH_FIRST)) {
            return new DepthFirstJSONParser(repeatingQName, inputStream);
        } else {
            throw new FactoryConfigurationError("The only parser implementation available is " + DEPTH_FIRST);
        }
    }
}
