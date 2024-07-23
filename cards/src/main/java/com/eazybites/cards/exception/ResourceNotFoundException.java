package com.eazybites.cards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends  RuntimeException{
    public  ResourceNotFoundException(String resource, String field , String value){
        super(String.format("%s Resource for field %s is not found for value %s",resource,field,value));
    }
}
