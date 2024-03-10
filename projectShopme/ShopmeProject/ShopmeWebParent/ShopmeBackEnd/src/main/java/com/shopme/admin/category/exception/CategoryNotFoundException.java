package com.shopme.admin.category.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;


public class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String message){
        super(message);
    }
}
