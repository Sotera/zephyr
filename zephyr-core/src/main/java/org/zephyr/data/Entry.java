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

import java.util.List;

/**
 * An Entry is a value with a label, a value (usually normalized and validated), a list of types, a visibility field for
 * analytics or databases that might require it (depending on which analytic or database it is, it may require some form of conversion), and a metadata field.
 * <p/>
 * The metadata field can be used in whatever way makes sense for your ETL process. One use for it can be that the same metadata
 * string in multiple entries indicates a grouping between them.
 * <p/>
 * You might put "source" in for metadata on both the latitude and longitude categorized values, and you might put "destination" in for metadata on the others.
 * <p/>
 * It is then up to your writer to determine how best to put this data into the final location.
 */
public class Entry {

    /**
     * The label for this entry
     */
    private String label;
    /**
     * The normalized, validated value
     */
    private String value;
    /**
     * A list of system defined types that this entry can behave as (e.g. float, latitude)
     */
    private List<String> types;
    /**
     * A visibility string that can be used for data storage mechanisms that support it
     */
    private String visibility;
    /**
     * A metadata field that can be used as your system sees fit
     */
    private String metadata;

    /**
     * Fully qualified constructor for an Entry
     *
     * @param label
     * @param value
     * @param types
     * @param visibility
     * @param metadata
     */
    public Entry(final String label, final String value, final List<String> types, final String visibility, final String metadata) {
        this.label = label;
        this.value = value;
        this.types = types;
        this.visibility = visibility;
        this.metadata = metadata;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the list of types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * @return the visibility
     */
    public String getVisibility() {
        return visibility;
    }

    /**
     * @return the metadata
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * @param metadata the metadata to set
     */
    public void setMetadata(final String metadata) {
        this.metadata = metadata;
    }

}