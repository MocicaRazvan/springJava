package com.moc.wellness.exception.notFound;

public class PrivateRouteException extends RuntimeException {
    public PrivateRouteException() {
        super("Not allowed!");
    }
}
