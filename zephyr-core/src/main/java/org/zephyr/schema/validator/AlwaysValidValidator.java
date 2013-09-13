package org.zephyr.schema.validator;

public class AlwaysValidValidator implements Validator {

    @Override
    public boolean isValid(String value) {
        return true;
    }

}
