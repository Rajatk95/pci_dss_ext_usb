package com.astrel.io.atomic;

class FileExistsException extends java.io.IOException {
    FileExistsException() {
    }

    FileExistsException(String message) {
        super(message);
    }

    FileExistsException(java.io.File f) {
        super(f.toString());
    }
}
