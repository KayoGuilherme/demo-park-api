package com.ky.demo_park_api.exception;

public class EntityNotFoundException extends RuntimeException  {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
