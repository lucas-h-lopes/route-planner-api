package com.study.projects.percursos_van.exception;

public class DuplicatedEmailException extends RuntimeException{
    public DuplicatedEmailException(String msg){
        super(msg);
    }
}
