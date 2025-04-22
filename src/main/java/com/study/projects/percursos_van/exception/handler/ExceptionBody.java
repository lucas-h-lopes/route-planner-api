package com.study.projects.percursos_van.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.TreeMap;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ExceptionBody {

    private String path;
    private String method;
    private Integer status;
    private String status_message;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public ExceptionBody(HttpServletRequest request, HttpStatus http, String message,Exception e){
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = http.value();
        this.status_message = http.getReasonPhrase();
        this.message = message;

        this.errors = getErrors(e);
    }

    private Map<String,String> getErrors(Exception e){
        if(e instanceof MethodArgumentNotValidException ex){
            Map<String,String> result = new TreeMap<>();
            for(FieldError error : ex.getFieldErrors()){
                result.put(error.getField(), error.getDefaultMessage());
            }
            return result;
        }
        return null;
    }
}
