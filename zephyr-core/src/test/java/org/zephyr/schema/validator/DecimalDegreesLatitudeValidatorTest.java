package org.zephyr.schema.validator;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.zephyr.schema.validator.DecimalDegreesLatitudeValidator;
import org.zephyr.schema.validator.Validator;

public class DecimalDegreesLatitudeValidatorTest {

    private Validator validator;

    @Before
    public void setup() {
        validator = new DecimalDegreesLatitudeValidator();
    }

    @Test
    public void testGoodData() {
        assertTrue(validator.isValid("-89.3333333333333333"));
        assertTrue(validator.isValid("89"));
        assertTrue(validator.isValid("0"));
        assertTrue(validator.isValid("89.555553"));
        assertTrue(validator.isValid("-33"));
    }

    @Test
    public void testBadData() {
        assertTrue(!validator.isValid("33g"));
        assertTrue(!validator.isValid("-91"));
        assertTrue(!validator.isValid("91"));
        assertTrue(!validator.isValid("180.0000000001"));
    }

}
