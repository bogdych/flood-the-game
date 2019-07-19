package com.summerschool.flood.server;

import lombok.Getter;

public class ServiceException extends RuntimeException {
    @Getter
    private final String message;

    public ServiceException(String message) {
        super();
        this.message = message;
    }
}
