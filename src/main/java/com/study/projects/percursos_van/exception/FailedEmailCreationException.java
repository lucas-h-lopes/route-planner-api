package com.study.projects.percursos_van.exception;

public class FailedEmailCreationException extends RuntimeException{
    public FailedEmailCreationException(String msg){
        super(msg);
    }
}
