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
