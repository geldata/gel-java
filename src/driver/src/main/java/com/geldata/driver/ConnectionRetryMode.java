package com.geldata.driver;

/**
 * An enum that specifies retry behavior for retryable errors when connecting to Gel.
 */
public enum ConnectionRetryMode {
    /**
     * The client should always retry the connection.
     */
    ALWAYS_RETRY,

    /**
     * The client should never retry the connection.
     */
    NEVER_RETRY
}
