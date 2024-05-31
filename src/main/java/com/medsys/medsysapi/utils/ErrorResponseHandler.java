package com.medsys.medsysapi.utils;

import com.medsys.medsysapi.db.QueryException;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class ErrorResponseHandler {
    static Logger logger = Logger.getLogger(ErrorResponseHandler.class.getName());

    public ResponseEntity queryExceptionHandler(QueryException e) {
        if(e.getStatus() == HTTPResponse.SC_SERVER_ERROR) {
            logger.severe(e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
            return generateErrorResponse(HTTPResponse.SC_SERVER_ERROR, e);
        }
        return new BasicResponse(e.getStatus(), e.getMessage()).generateResponse();
    }


    public ResponseEntity notFoundHandler(NoResourceFoundException e) {
        return generateErrorResponse(HTTPResponse.SC_NOT_FOUND, e);
    }

    public ResponseEntity methodNotAllowedHandler(HttpRequestMethodNotSupportedException e) {
        return generateErrorResponse(405, e);
    }

    public ResponseEntity badRequestHandler(Exception e) {
        return generateErrorResponse(400, e);
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity otherExceptionHandler(Throwable e) {
        if(e instanceof MissingRequestHeaderException) {
            return badRequestHandler((MissingRequestHeaderException) e);
        }
        if(e instanceof QueryException) {
            return queryExceptionHandler((QueryException) e);
        }
        if(e instanceof NoResourceFoundException) {
            return notFoundHandler((NoResourceFoundException) e);
        }
        if(e instanceof HttpRequestMethodNotSupportedException) {
            return methodNotAllowedHandler((HttpRequestMethodNotSupportedException) e);
        }

        logger.log(Level.SEVERE, e.getMessage(), e);
        return generateErrorResponse(HTTPResponse.SC_SERVER_ERROR, new Exception("A critical error occurred!"));
    }

    public static ResponseEntity generateErrorResponse(int status, Exception e) {
        return new BasicResponse(status, e.getMessage()).generateResponse();
    }
}
