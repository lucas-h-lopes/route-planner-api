package com.study.projects.percursos_van.validation.anotation;

import com.study.projects.percursos_van.validation.validator.PhoneArrayValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = PhoneArrayValidator.class)
public @interface PhoneArray {

    String message() default "Os elementos do array são obrigatórios e devem possuir 11 caracteres";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
