package com.study.projects.percursos_van.exception;

public class DuplicatedPhoneNumberException extends RuntimeException{
    public DuplicatedPhoneNumberException(String msg){
        super(msg);
    }
}
