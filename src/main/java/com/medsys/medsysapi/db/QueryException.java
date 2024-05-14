package com.medsys.medsysapi.db;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;

public class QueryException extends Exception {
    private int status = HTTPResponse.SC_SERVER_ERROR;
    private Throwable cause = null;

    public QueryException(Throwable cause) {
        super(cause);
        this.cause = cause;
    }

    public QueryException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }

    public QueryException(int status, Throwable cause) {
        super(cause);
        this.status = status;
        this.cause = cause;
    }

    public QueryException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.cause = cause;
    }

    public QueryException(String message) {
        super(message);
    }

    public QueryException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public Throwable getCause() {
        return cause;
    }
}
