package org.zephyr.schema.validator;

public class LongValidator implements Validator {

    @Override
    public boolean isValid(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
