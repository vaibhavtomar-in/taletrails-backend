package com.taletrails.taletrails_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;
import com.taletrails.taletrails_backend.exception.AccessDeniedException;
import com.taletrails.taletrails_backend.exception.LogitrackException;
import com.taletrails.taletrails_backend.exception.ResourceNotFoundException;
import com.taletrails.taletrails_backend.exception.LogitracError;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException exception) {

        LOGGER.info(exception.getError() + " --> " + exception.getMessage());

        LogitracError error = exception.getError();

        Map<String, String> response = new HashMap<String, String>();
        response.put("errorCode", error.getCode());
        response.put("errorShortDescription", error.getShortDesc());
        response.put("errorDescription", exception.getMessage());

        return new ResponseEntity<Map<String, String>>(response, HttpStatus.FORBIDDEN);
    }



    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String, String>> unHandledError(Exception exception) {
        LOGGER.error(exception.getMessage(), exception);
        Map<String, String> response = new HashMap<String, String>();
        response.put("message", LogitracError.SERVER_ERROR.toString() + " --> " + exception.getMessage());
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LogitrackException.class)
    public final ResponseEntity<Map<String, String>> handleLogitracException(LogitrackException exception) {

        LOGGER.info(exception.getError() + " --> " + exception.getMessage());

        LogitracError error = exception.getError();

        Map<String, String> response = new HashMap<String, String>();
        response.put("errorCode", error.getCode());
        response.put("errorShortDescription", error.getShortDesc());
        response.put("errorDescription", error.getDescription());

        if (exception instanceof ResourceNotFoundException) {
            return new ResponseEntity<Map<String, String>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
    }


}
