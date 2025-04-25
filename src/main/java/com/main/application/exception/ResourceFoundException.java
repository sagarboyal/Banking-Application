package com.main.application.exception;

public class ResourceFoundException extends RuntimeException{
    String resourceName;
    String fieldName;

    public ResourceFoundException(String resourceName, String fieldName){
        super(String.format("Resource %s is already exists for field %s", resourceName, fieldName));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
    }
}
