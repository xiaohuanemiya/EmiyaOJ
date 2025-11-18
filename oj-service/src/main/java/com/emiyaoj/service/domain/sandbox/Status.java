package com.emiyaoj.service.domain.sandbox;

public enum Status {
    Accepted("Accepted"),
    MemoryLimitExceeded("Memory Limit Exceeded"),
    TimeLimitExceeded("Time Limit Exceeded"),
    OutputLimitExceeded("Output Limit Exceeded"),
    FileError("File Error"),
    NonzeroExitStatus("Nonzero Exit Status"),
    Signalled("Signalled"),
    InternalError("Internal Error");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
