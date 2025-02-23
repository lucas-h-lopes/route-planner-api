package com.study.projects.percursos_van.exception.handler;

import com.study.projects.percursos_van.exception.DuplicatedEmailException;
import com.study.projects.percursos_van.exception.InvalidCredentialsException;
import com.study.projects.percursos_van.exception.InvalidRoleException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ExceptionBody> conflictException(Exception e, HttpServletRequest request){
        log.info("Exceção conflict lançada: ", e);
        return ResponseEntity.badRequest()
                .body(new ExceptionBody(request, HttpStatus.BAD_REQUEST, e.getMessage(), e));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> methodArgumentNotValidException(Exception e, HttpServletRequest request){
        log.info("Exceção validation lançada: ", e);
        return ResponseEntity.badRequest()
                .body(new ExceptionBody(request, HttpStatus.BAD_REQUEST, "Erro de validação", e));
    }
}
