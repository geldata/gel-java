package com.geldata.driver.binary.protocol;

public enum ProtocolPhase {
    CONNECTION,
    AUTH,
    COMMAND,
    DUMP,
    TERMINATION,
    ERRORED;
}
