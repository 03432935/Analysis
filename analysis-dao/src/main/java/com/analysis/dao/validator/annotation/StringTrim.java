package com.analysis.dao.validator.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/14 18:49
 */
@Target({ElementType.METHOD,ElementType.TYPE, ANNOTATION_TYPE, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited//允许子类继承父类
public @interface StringTrim {
}
