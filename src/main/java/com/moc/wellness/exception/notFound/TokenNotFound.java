package com.moc.wellness.exception.notFound;

public class TokenNotFound extends NotFoundError {

    public TokenNotFound() {
        super("A valid token wasn't provided!");
    }
}
