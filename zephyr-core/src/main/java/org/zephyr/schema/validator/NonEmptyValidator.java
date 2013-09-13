package org.zephyr.schema.validator;

public class NonEmptyValidator implements Validator {

    @Override
    public boolean isValid(String value) {
        return (value != null && !value.trim().isEmpty());
    }

}
