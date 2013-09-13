package org.zephyr.enricher;

import org.zephyr.data.Record;

/**
 * Enricher contract for Zephyr
 */
public interface Enricher {

    /**
     * The enrich method MUST either:
     * - Leave the original record as it was in case of un-enrichable data
     * or
     * - return the modified record on success
     * or
     * - throw a RuntimeException in case of exceptional circumstance (exception is logged and processing continues)
     *
     * @param record incoming record to be enriched
     */
    void enrich(Record record);

}
