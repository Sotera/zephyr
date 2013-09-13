package org.zephyr.schema.normalizer;

public class SplitNormalizer implements Normalizer {

    private String regex;
    private int returnIndex;

    public SplitNormalizer(String regex, int returnIndex) {
        this.regex = regex;
        this.returnIndex = returnIndex;
    }

    @Override
    public String normalize(String value) throws NormalizationException {
        try {
            String[] values = value.split(regex);
            return values[returnIndex];
        } catch (Throwable t) {
            throw new NormalizationException("An exception occurred when calling String.split()[" + returnIndex + "] on the given value, " + value + ".", t);
        }
    }

}
