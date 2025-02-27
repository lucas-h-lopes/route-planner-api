package com.study.projects.percursos_van.exception.handler;

import com.study.projects.percursos_van.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalHandler {

    @ExceptionHandler({InvalidCredentialsException.class, InvalidRoleException.class})
    public ResponseEntity<ExceptionBody> badRequestException(Exception e, HttpServletRequest request) {
        log.info("Exceção bad request lançada: ", e);
        return ResponseEntity.badRequest()
                .body(new ExceptionBody(request, HttpStatus.BAD_REQUEST, e.getMessage(), e));
    }

    @ExceptionHandler({DuplicatedEmailException.class, DuplicatedCpfException.class, DuplicatedCnhException.class})
    public ResponseEntity<ExceptionBody> conflictException(Exception e, HttpServletRequest request) {
        log.info("Exceção conflict lançada: ", e);
        return ResponseEntity.badRequest()
                .body(new ExceptionBody(request, HttpStatus.BAD_REQUEST, e.getMessage(), e));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionBody> notFoundException(NotFoundException e, HttpServletRequest request){
        log.info("Exceção not found lançada: ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                .body(new ExceptionBody(request, HttpStatus.NOT_FOUND, e.getMessage(), e));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionBody> accessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        log.info("Exceção acesso negado: ", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionBody(request, HttpStatus.FORBIDDEN, e.getMessage(), e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> methodArgumentNotValidException(Exception e, HttpServletRequest request) {
        log.info("Exceção validation lançada: ", e);
        return ResponseEntity.badRequest()
                .body(new ExceptionBody(request, HttpStatus.BAD_REQUEST, "Erro de validação", e));
    }
}
