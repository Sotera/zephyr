package org.zephyr.schema.normalizer;

public class ReplaceAllNormalizer implements Normalizer {

    private String regex;
    private String replacement;

    public ReplaceAllNormalizer(String regex, String replacement) {
        this.regex = regex;
        this.replacement = replacement;
    }

    @Override
    public String normalize(String value) throws NormalizationException {
        try {
            return value.replaceAll(regex, replacement);
        } catch (Throwable t) {
            throw new NormalizationException("An exception occurred when calling String.replaceAll() on the given value, " + value + ", with the regex: " + regex + " and the replacement value [" + replacement + "].", t);
        }
    }

}
