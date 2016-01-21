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
