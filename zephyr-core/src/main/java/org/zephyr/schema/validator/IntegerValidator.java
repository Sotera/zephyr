package org.zephyr.schema.validator;

public class IntegerValidator implements Validator {

    @Override
    public boolean isValid(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
