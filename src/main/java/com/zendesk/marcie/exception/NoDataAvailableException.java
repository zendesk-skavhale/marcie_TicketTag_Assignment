package com.zendesk.marcie.exception;

import org.springframework.web.client.RestClientException;

public class NoDataAvailableException extends Exception {

    public NoDataAvailableException(String message) {
        super(message);
    }

    public NoDataAvailableException(String string, RestClientException ex) {
    }
}
