package org.zephyr.schema.validator;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.zephyr.schema.validator.DecimalDegreesLongitudeValidator;
import org.zephyr.schema.validator.Validator;

public class DecimalDegreesLongitudeValidatorTest {

    private Validator validator;

    @Before
    public void setup() {
        validator = new DecimalDegreesLongitudeValidator();
    }

    @Test
    public void testGoodData() {
        assertTrue(validator.isValid("-179.3333333333333333"));
        assertTrue(validator.isValid("179"));
        assertTrue(validator.isValid("0"));
        assertTrue(validator.isValid("89.555553"));
        assertTrue(validator.isValid("-180"));
    }

    @Test
    public void testBadData() {
        assertTrue(!validator.isValid("33q"));
        assertTrue(!validator.isValid("-181"));
        assertTrue(!validator.isValid("190"));
        assertTrue(!validator.isValid("180.0000000001"));
    }

}
