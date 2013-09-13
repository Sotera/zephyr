package org.zephyr.parser;

import java.io.IOException;
import java.util.List;

import org.zephyr.data.Pair;
import org.zephyr.data.ProcessingResult;

/**
 * Parser contract for Zephyr
 */
public abstract class Parser {

    public static final String SOURCE_ELEMENT_IDENTIFIER_HEADER = "source_element_identifier_header";

    /**
     * When we are operating over data that is sourced from a file, we would like to be capable
     * of returning the filename as a Pair in our parse method - even if we ultimately ignore it
     * in our eventual output.  In some cases, however, it might be essential.
     * <p/>
     * For sources that are sockets, queues, etc, where "filenames" don't make sense, we leave it up to
     * the binding implementation to determine what this should be - or if it should just be null.
     */
    private String sourceElementIdentifier = null;

    public String getSourceElementIdentifier() {
        return this.sourceElementIdentifier;
    }

    public void setSourceElementIdentifier(String sourceElementIdentifier) {
        this.sourceElementIdentifier = sourceElementIdentifier;
    }

    public Pair getSourceElementPair() {
        return new Pair(SOURCE_ELEMENT_IDENTIFIER_HEADER, this.sourceElementIdentifier);
    }

    /**
     * The parse method will parse the next list of key:value pairs out of the
     *
     * @return
     * @throws IOException
     */
    public abstract ProcessingResult<List<Pair>, byte[]> parse() throws IOException;

}
