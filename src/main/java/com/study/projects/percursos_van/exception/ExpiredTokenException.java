package com.study.projects.percursos_van.exception;

public class ExpiredTokenException extends RuntimeException{
    public ExpiredTokenException(String msg){
        super(msg);
    }
}
