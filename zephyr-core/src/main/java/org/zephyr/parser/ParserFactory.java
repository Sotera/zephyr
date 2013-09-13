package org.zephyr.parser;

import java.io.InputStream;

/**
 * ParserFactory contract for Zephyr; an implementation of ParserFactory will likely have Parser configuration specific
 * parameters specified on them.  When {@link #newParser(InputStream)} is called, a new Parser instance (configured as per
 * the implementing ParserFactory's specification) is created to operate over the provided InputStream.
 */
public interface ParserFactory {

    /**
     * @param inputStream the {@link java.io.InputStream} the parser will operate over
     * @return the parser that will parse records from the provided {@link java.io.InputStream}
     */
    Parser newParser(InputStream inputStream);

}
