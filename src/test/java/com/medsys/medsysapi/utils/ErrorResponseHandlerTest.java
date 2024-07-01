package com.medsys.medsysapi.utils;

import com.medsys.medsysapi.db.QueryException;
import com.nimbusds.oauth2.sdk.http.HTTPRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.lang.reflect.Method;
import java.util.List;

class ErrorResponseHandlerTest {
    ErrorResponseHandler errorResponseHandler = new ErrorResponseHandler();

    @Mock
    MissingRequestHeaderException missingRequestHeaderException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOtherExceptionHandler() {
        Throwable mockThrowable;
        ResponseEntity responseEntity;

        // Test Bad Request
        mockThrowable = missingRequestHeaderException;
        responseEntity = errorResponseHandler.otherExceptionHandler(mockThrowable);
        Assertions.assertEquals(400, responseEntity.getStatusCode().value());

        mockThrowable = new HttpMessageNotReadableException("message");
        responseEntity = errorResponseHandler.otherExceptionHandler(mockThrowable);
        Assertions.assertEquals(400, responseEntity.getStatusCode().value());

        // Test Query Exception
        mockThrowable = new QueryException(555, "message");
        responseEntity = errorResponseHandler.otherExceptionHandler(mockThrowable);
        Assertions.assertEquals(555, responseEntity.getStatusCode().value());

        // Test Not Found Exception
        mockThrowable = new NoResourceFoundException(HttpMethod.GET, "url");
        responseEntity = errorResponseHandler.otherExceptionHandler(mockThrowable);
        Assertions.assertEquals(404, responseEntity.getStatusCode().value());

        // Test Method Not Allowed Exception
        mockThrowable = new HttpRequestMethodNotSupportedException("GET");
        responseEntity = errorResponseHandler.otherExceptionHandler(mockThrowable);
        Assertions.assertEquals(405, responseEntity.getStatusCode().value());

        // Test Server Error
        mockThrowable = new Exception("message");
        responseEntity = errorResponseHandler.otherExceptionHandler(mockThrowable);
        Assertions.assertEquals(500, responseEntity.getStatusCode().value());

    }
}