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
