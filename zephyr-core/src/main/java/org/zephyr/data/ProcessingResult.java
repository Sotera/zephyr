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
