package org.zephyr.schema.validator;

public class DecimalDegreesLatitudeValidator implements Validator {
    @Override
    public boolean isValid(String value) {
        try {
            Double doubleValue = Double.parseDouble(value);
            return doubleValue <= 90 && doubleValue >= -90;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
