/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.zephyr.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zephyr.util.UUIDHelper;

/**
 * An record of data that contains 0..N Entries, in order, and provides accessor methods
 * to them.  It has an identifier that will be generated for it if one is not provided to it.
 */
public class Record implements Iterable<Entry> {

    private String uuid;
    private String feedName;
    private List<Entry> entries;

    /**
     * No argument constructor that specifies a newly generated UUID and an empty feed name for this record
     */
    public Record() {
        this(UUIDHelper.generateUUID(), "");
    }

    /**
     * Creates a new Record object with the provided UUID and feedName (and sets the list of entries to be a new ArrayList)
     *
     * @param uuid
     * @param feedName
     */
    public Record(final String uuid, final String feedName) {
        this.uuid = uuid;
        this.feedName = feedName;
        this.entries = new ArrayList<Entry>();
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return this.uuid;
    }

    /**
     * @return the feedName
     */
    public String getFeedName() {
        return this.feedName;
    }

    /**
     * @param feedName The feed which provided us with this record
     */
    public void setFeedName(final String feedName) {
        this.feedName = feedName;
    }

    /**
     * Add a non-null entry to the record
     *
     * @param entry
     */
    public void add(Entry entry) {
        if (entry == null)
            throw new NullPointerException("The entry provided to this record was null");
        this.entries.add(entry);
    }

    /**
     * Add 1..N entries to the record, in order
     *
     * @param entries
     */
    public void add(Entry... entries) {
        if (entries == null)
            throw new NullPointerException("The varargs list of entries provided was null or empty");
        for (Entry value : entries) {
            add(value);
        }
    }

    /**
     * @return The number of entries in this record
     */
    public int size() {
        return this.entries.size();
    }

    /**
     * @param index
     * @return The entry at this index
     */
    public Entry get(int index) {
        return this.entries.get(index);
    }

    /**
     * Allows us to iterate over the array list of entries this record is composed of
     */
    @Override
    public Iterator<Entry> iterator() {
        return this.entries.iterator();
    }

}
