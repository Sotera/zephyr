package org.zephyr.output;

import java.io.Closeable;
import java.io.Flushable;

import org.zephyr.data.Record;

public abstract class Outputter implements Flushable, Closeable {

    public abstract void initialize() throws Exception;

    public abstract void output(Record record) throws Exception;

}
