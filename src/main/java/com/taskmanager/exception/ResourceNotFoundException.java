package com.taskmanager.exception;


//Exception thrown when a requested resource is not found.
public class ResourceNotFoundException extends RuntimeException {


    //Constructs a new ResourceNotFoundException with the specified detail message.
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
