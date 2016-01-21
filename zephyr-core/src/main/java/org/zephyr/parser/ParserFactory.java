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
