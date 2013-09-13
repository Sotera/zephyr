package org.zephyr.schema.validator;

public class DoubleValidator implements Validator {

    @Override
    public boolean isValid(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
