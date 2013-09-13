package org.zephyr.schema.validator;

import javax.xml.bind.DatatypeConverter;

public class IsoDateFormatValidator implements Validator {

    @Override
    public boolean isValid(String value) {
        try {
            /*	initial formats we're concerned about
				YYYY-MM-DDTHH:MM:SSZ
				YYYY-MM-DDTHH:MM:SS.SSSZ
			 */
            DatatypeConverter.parseDateTime(value);
            return true;
        } catch (IllegalArgumentException iae) {
            return false;
        }
    }

}
