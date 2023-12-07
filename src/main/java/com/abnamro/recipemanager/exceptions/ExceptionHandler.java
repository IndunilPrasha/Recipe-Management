package com.abnamro.recipemanager.exceptions;
/* Created by IndunilPrasanna */

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//TODO
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = RecipeNotFoundException.class)
    public ResponseEntity<?> handleRecipeNotFound(RecipeNotFoundException recipeNotFoundException){
        return new ResponseEntity<>(new RecipeNotFoundException(recipeNotFoundException.getMessage()), HttpStatus.NOT_FOUND);
    }
}
