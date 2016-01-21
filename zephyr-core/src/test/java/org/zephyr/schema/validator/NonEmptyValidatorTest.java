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
package org.zephyr.schema.validator;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.zephyr.schema.validator.NonEmptyValidator;
import org.zephyr.schema.validator.Validator;

public class NonEmptyValidatorTest {

    private Validator validator;

    @Before
    public void setup() {
        validator = new NonEmptyValidator();
    }

    @Test
    public void testGoodData() {
        assertTrue(validator.isValid("d"));
        assertTrue(validator.isValid("12350918509185"));
    }

    @Test
    public void testBadData() {
        assertTrue(!validator.isValid(""));
        assertTrue(!validator.isValid(null));
        assertTrue(!validator.isValid("                 "));
        assertTrue(!validator.isValid("\n"));
        assertTrue(!validator.isValid("\t\t\t"));
        assertTrue(!validator.isValid("\u0000"));
    }

}
