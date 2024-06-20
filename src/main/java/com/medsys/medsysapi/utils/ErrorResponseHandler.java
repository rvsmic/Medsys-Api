package com.medsys.medsysapi.utils;

import com.medsys.medsysapi.db.QueryException;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ErrorResponseHandler {
    static Logger logger = LoggerFactory.getLogger(ErrorResponseHandler.class);

    public ResponseEntity queryExceptionHandler(QueryException e) {
        if(e.getStatus() == HTTPResponse.SC_SERVER_ERROR) {
            logger.error(e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
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
        if(e instanceof HttpMessageNotReadableException) {
            return badRequestHandler((HttpMessageNotReadableException) e);
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

        logger.error(e.getMessage(), e);
        return generateErrorResponse(HTTPResponse.SC_SERVER_ERROR, new Exception("A critical error occurred!"));
    }

    public ResponseEntity generateErrorResponse(int status, Exception e) {
        return new BasicResponse(status, e.getMessage()).generateResponse();
    }
}
