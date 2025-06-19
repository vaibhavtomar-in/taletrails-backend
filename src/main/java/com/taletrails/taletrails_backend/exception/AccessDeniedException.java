package com.taletrails.taletrails_backend.exception;

public class AccessDeniedException extends LogitrackException {
    public AccessDeniedException() {
        super(LogitracError.ACCESS_DENIED);
    }

    public AccessDeniedException(String message) {
        super(LogitracError.ACCESS_DENIED, message);
    }
}
