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
