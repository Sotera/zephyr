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
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.zephyr.schema.validator.IsoDateFormatValidator;
import org.zephyr.schema.validator.Validator;

public class IsoDateFormatValidatorTest {

    private Validator validator;

    @Before
    public void setup() {
        /*	initial formats we're concerned about
				YYYY-MM-DDTHH:MM:SSZ
				YYYY-MM-DDTHH:MM:SS.SSSZ
		*/
        validator = new IsoDateFormatValidator();
    }


    @Test
    public void testText() {
        assertFalse(validator.isValid("some text in here"));
        assertFalse(validator.isValid("2013-03-12asdf"));
        assertFalse(validator.isValid("real date in here 2013-05-16"));
    }

    @Test
    public void testOutOfBoundsDates() {
        assertFalse(validator.isValid("2013-02-29T01:54:30.207Z")); //shouldn't have leap year in 2013
        assertFalse(validator.isValid("1900-02-29T01:54:30.207Z")); //shouldn't have leap year in 1900 (divided by 100)
        assertFalse(validator.isValid("2013-03-29T25:54:30.207Z")); //no 25th hour

    }

    @Test
    public void testWrongFormat() {
        assertFalse(validator.isValid("01/13/2013"));
        assertFalse(validator.isValid("2013/01/01"));
    }

    @Test
    public void testGoodDates() {
        assertTrue(validator.isValid("2011-03-10T11:54:30.207Z"));
        assertTrue(validator.isValid("2011-03-10T11:54:30Z"));
        assertTrue(validator.isValid("2012-02-29T01:54:30.207Z"));  //leap year
        assertTrue(validator.isValid("2000-02-29T01:54:30.207Z"));  //leap year on millennials
        assertTrue(validator.isValid("2013-02-17"));
        assertTrue(validator.isValid("2013-02-17Z"));
    }

}
