package com.study.projects.percursos_van.exception;

public class MismatchedPasswordException extends RuntimeException{
    public MismatchedPasswordException(String message){
        super (message);
    }
}
