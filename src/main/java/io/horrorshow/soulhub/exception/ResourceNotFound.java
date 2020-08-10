package io.horrorshow.soulhub.exception;

public class ResourceNotFound extends RuntimeException {

    private static final long serialVersionUID = -5011564040723908377L;

    public ResourceNotFound(String message) {
        super(message);
    }

    public ResourceNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
