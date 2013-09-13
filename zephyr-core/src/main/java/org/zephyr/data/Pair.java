package org.zephyr.data;

/**
 * A key-value pairing of data
 */
public class Pair {

    /**
     * The key
     */
    private String key;
    /**
     * The value
     */
    private String value;

    /**
     * Constructor that takes in the key and value
     *
     * @param key
     * @param value
     */
    public Pair(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * Human readable toString override.
     */
    @Override
    public String toString() {
        return "Pair [key=" + key + ", value=" + value + "]";
    }

}
