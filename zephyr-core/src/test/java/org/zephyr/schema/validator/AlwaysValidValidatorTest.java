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
