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
import org.zephyr.schema.validator.AlwaysValidValidator;
import org.zephyr.schema.validator.Validator;

public class AlwaysValidValidatorTest {

    private Validator validator;

    @Before
    public void setup() {
        validator = new AlwaysValidValidator();
    }

    @Test
    public void testAlwaysValidValidator() {
        assertTrue(validator.isValid("foo"));
        assertTrue(validator.isValid("bar"));
        assertTrue(validator.isValid(null));
    }

}
