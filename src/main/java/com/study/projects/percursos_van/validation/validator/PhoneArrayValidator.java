package com.study.projects.percursos_van.validation.validator;

import com.study.projects.percursos_van.validation.anotation.PhoneArray;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PhoneArrayValidator implements ConstraintValidator<PhoneArray, String[]> {
    @Override
    public boolean isValid(String[] values, ConstraintValidatorContext constraintValidatorContext) {
        if (values == null) {
            return true;
        }

        for (String value : values) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            if (value == null || value.length() != 11) {
                constraintValidatorContext.buildConstraintViolationWithTemplate(
                        "O número '" + value + "' deve ter exatamente 11 caracteres"
                ).addConstraintViolation();
                return false;
            }
        }
        final Set<String> unique = new HashSet<>(Arrays.asList(values));

        if (unique.size() != values.length) {
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                    "Os telefones informados não podem ser iguais"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
