package org.zephyr.output.formatter;

import org.zephyr.data.Record;

/**
 * An OutputFormatter class converts a Record into the format we want to write to disk in the case of any sort of output operation wherein we write to disk
 * <p/>
 * It can either be used by a Writer class or directly.
 */

public interface OutputFormatter {

    byte[] formatRecord(Record record);

}
