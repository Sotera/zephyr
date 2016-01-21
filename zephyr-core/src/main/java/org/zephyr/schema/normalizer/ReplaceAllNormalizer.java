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
package org.zephyr.schema.normalizer;

public class ReplaceAllNormalizer implements Normalizer {

    private String regex;
    private String replacement;

    public ReplaceAllNormalizer(String regex, String replacement) {
        this.regex = regex;
        this.replacement = replacement;
    }

    @Override
    public String normalize(String value) throws NormalizationException {
        try {
            return value.replaceAll(regex, replacement);
        } catch (Throwable t) {
            throw new NormalizationException("An exception occurred when calling String.replaceAll() on the given value, " + value + ", with the regex: " + regex + " and the replacement value [" + replacement + "].", t);
        }
    }

}
