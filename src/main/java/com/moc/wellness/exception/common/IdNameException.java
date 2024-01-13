package com.moc.wellness.exception.common;

import lombok.Getter;

@Getter
public class IdNameException extends RuntimeException {
    private final String name;
    private final Long id;

    public IdNameException(String name, Long id, String message) {
        super(message);
        this.name = name;
        this.id = id;
    }
}
