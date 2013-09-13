package org.zephyr.schema.validator;

public class DecimalDegreesLongitudeValidator implements Validator {

    @Override
    public boolean isValid(String value) {
        try {
            Double doubleValue = Double.parseDouble(value);
            return doubleValue <= 180 && doubleValue >= -180;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
