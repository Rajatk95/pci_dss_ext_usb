package com.astrel.io.atomic;

public class InconsistentStateException extends NestedException {
    InconsistentStateException() {
    }

    InconsistentStateException(Exception e) {
        super(e);
    }
}
