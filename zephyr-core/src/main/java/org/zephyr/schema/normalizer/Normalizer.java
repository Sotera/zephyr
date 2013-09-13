package org.zephyr.schema.normalizer;

public interface Normalizer {

    String normalize(String value) throws NormalizationException;

}
