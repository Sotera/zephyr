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
