package com.astrel.io.atomic;

import java.io.File;
import java.io.IOException;

abstract class OpenFileAction extends Action {
    protected File original;
    private File backup;
    private boolean exist;                // did the file exist prior to being opened?
    private transient boolean truncate;

    OpenFileAction(File original, boolean truncate) {
        this.original = original;
        this.truncate = truncate;
    }

    protected void prepare() {
        backup = generateBackupFilename(original);
        exist = original.exists();
    }

    protected void createBackup() throws IOException {
        if (exist) {
            if (truncate)
                renameNotDeleting(original, backup);
            else
                copyNotDeleting(original, backup);
        }
    }

    protected void undo() throws IOException {
        if (exist)
            restoreBackup(backup, original);
        else
            deleteIfExists(original);
    }

    protected void cleanup() throws IOException {
        deleteIfExists(backup);
    }

    public String toString() {
        return "[" + getClass().getName() +
                ": original=" + original + ", backup=" + backup + "]";
    }
}
