package com.hzy.rwfile;

import java.io.IOException;

/**
 * @author hzy
 * @since 3/27/2021
 */
public class RwFileException extends IOException {
    /**
     * The error message string
     */
    protected final String message;

    /**
     * Construct an exception instance.
     *
     * @param message A message string
     */
    public RwFileException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
