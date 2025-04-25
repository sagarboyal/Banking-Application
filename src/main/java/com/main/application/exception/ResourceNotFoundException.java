package com.main.application.exception;

public class ResourceNotFoundException extends RuntimeException{
    String resourceName;
    String fieldName;

    public ResourceNotFoundException(String resourceName, String fieldName){
        super(String.format("Resource is not found with %s: %s", fieldName, resourceName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
    }
}
