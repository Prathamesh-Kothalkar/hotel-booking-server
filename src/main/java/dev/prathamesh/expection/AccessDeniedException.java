package dev.prathamesh.expection;
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) { super(message); }
}