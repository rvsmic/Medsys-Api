package com.medsys.medsysapi.utils;

import com.medsys.medsysapi.db.QueryException;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class ErrorResponseHandler {
    static Logger logger = Logger.getLogger(ErrorResponseHandler.class.getName());

    @Order(1)
    @ExceptionHandler({QueryException.class})
    public ResponseEntity queryExceptionHandler(QueryException e) {
        if(e.getStatus() == HTTPResponse.SC_SERVER_ERROR) {
            logger.severe(e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
            return generateErrorResponse(HTTPResponse.SC_SERVER_ERROR, e);
        }
        return new BasicResponse(e.getStatus(), e.getMessage()).generateResponse();
    }

    @Order(1)
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity badRequestHandler(BadRequestException e) {
        return generateErrorResponse(HTTPResponse.SC_BAD_REQUEST, e);
    }

    @Order(1)
    @ExceptionHandler({NoResourceFoundException.class})
    public ResponseEntity notFoundHandler(NoResourceFoundException e) {
        return generateErrorResponse(HTTPResponse.SC_NOT_FOUND, e);
    }

    @Order(1)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity methodNotAllowedHandler(HttpRequestMethodNotSupportedException e) {
        return generateErrorResponse(405, e);
    }

    @Order(2)
    @ExceptionHandler({Throwable.class})
    public ResponseEntity otherExceptionHandler(Throwable e) {
        logger.log(Level.SEVERE, e.getMessage(), e);
        return generateErrorResponse(HTTPResponse.SC_SERVER_ERROR, new Exception("A critical error occurred!"));
    }

    public static ResponseEntity generateErrorResponse(int status, Exception e) {
        return new BasicResponse(status, e.getMessage()).generateResponse();
    }
}
