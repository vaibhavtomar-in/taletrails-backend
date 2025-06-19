package com.taletrails.taletrails_backend.exception;

public class LogitrackException extends RuntimeException {

    private final LogitracError error;

    public LogitrackException(LogitracError e) {
        super(e.getShortDesc());
        this.error = e;
    }
    public LogitrackException(LogitracError e, String message) {
        super(message);
        this.error = e;
    }

    public LogitracError getError() {
        return error;
    }

}
