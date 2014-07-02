/*
* Licensed to the Apache Software Foundation (ASF) under one
* or more contributor license agreements. See the NOTICE file
* distributed with this work for additional information
* regarding copyright ownership. The ASF licenses this file
* to you under the Apache License, Version 2.0 (the
* "License"); you may not use this file except in compliance
* with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.zephyr.data;

/**
 * We have two primary steps in Zephyr that could result in either a successful operation or exceptional behavior.
 * Since we are often operating over mini batches, we needed a way to consolidate the return of each step as one of these two
 * states without actually throwing an Exception (and prematurely ending the entire of our processing step over our small batch).
 * <p/>
 * This parameterized class allows us to send out either the successful result or a Throwable and the raw data (if possible) that caused
 * the throwable to be raised.
 *
 * @param <T> The type of our successfully processed data
 * @param <R> The type of our unsuccessfully processed raw data (or as close to it as can be generated, primarily for logging purposes)
 */
public class ProcessingResult<T, R> {

    /**
     * The data that was successfully processed
     */
    private T processedData;

    /**
     * The raw data (best effort) that was unsuccessfully processed
     */
    private R rawData;
    /**
     * The throwable that was thrown when processing the raw data
     */
    private Throwable error;

    /**
     * Constructor for properly processed data
     *
     * @param processedData
     */
    public ProcessingResult(T processedData) {
        this.processedData = processedData;
    }

    /**
     * Constructor for a failed processing step
     *
     * @param rawData
     * @param error
     */
    public ProcessingResult(R rawData, Throwable error) {
        this.rawData = rawData;
        this.error = error;
    }

    /**
     * @return true if the data were processed successfully
     */
    public boolean wasProcessedSuccessfully() {
        return processedData != null;
    }

    /**
     * @return the processed data
     */
    public T getProcessedData() {
        return processedData;
    }

    /**
     * @return best effort attempt at the raw data that was being operated over when the processing step failed
     */
    public R getRawData() {
        return rawData;
    }

    /**
     * @return Some throwable (almost invariably an Exception)
     */
    public Throwable getError() {
        return error;
    }

}
