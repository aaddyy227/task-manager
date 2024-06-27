package com.taskmanager.exception;


//Exception thrown when a duplicate task is encountered.
public class DuplicateTaskException extends RuntimeException {


    //Constructs a new DuplicateTaskException with the specified detail message.

    public DuplicateTaskException(String message) {
        super(message);
    }
}
