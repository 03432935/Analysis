package com.analysis.dao.validator.constraint;

import com.analysis.dao.validator.annotion.LongNotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/4 15:52
 */
public class LongValidator implements ConstraintValidator<LongNotNull, Long> {


    @Override
    public void initialize(LongNotNull constraintAnnotation) {

    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return true;
    }
}