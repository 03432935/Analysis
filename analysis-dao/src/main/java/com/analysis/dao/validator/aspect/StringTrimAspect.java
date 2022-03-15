package com.analysis.dao.validator.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/3/14 18:50
 */
@Component
@Aspect
public class StringTrimAspect{

    //通过pointcut获取到注解的位置
    @Pointcut("@annotation(com.analysis.dao.validator.annotation.StringTrim)")
    public void stringPointCut(){

    }

    //增强处理
    @Around("stringPointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        //获取参数值
        Object[] paramValues = proceedingJoinPoint.getArgs();
        for (Object o : paramValues){
            //获取类的字段getFields()：获得某个类的所有的公共（public）的字段，包括父类中的字段。
            //getDeclaredFields()：获得某个类的所有声明的字段，即包括public、private和proteced，但是不包括父类的申明字段。
            Field[] fields = o.getClass().getDeclaredFields();
            for(Field field : fields){
                //设置字段可访问
                field.setAccessible(true);
                //获取字段的名字
                String name = field.getName();
                //获取字段的类型
                String type = field.getGenericType().toString();

                //拼装get set方法
                String getMethodName = "get"+name.substring(0,1).toUpperCase()+name.substring(1);
                String setMethodName = "set"+name.substring(0,1).toUpperCase()+name.substring(1);

                //通过方法名称获取对应的方法，这里是获取字段的get方法
                Method getMethod = o.getClass().getMethod(getMethodName);

//class1.isAssignableFrom(class2) 判定 class1对象所表示的类或接口与class2 参数所表示的类或接口是否相同，
//或是否是其超类或超接口。如果是则返回 true；否则返回 false。如果该 class1表示一个基本类型，且指定的 class2参数正是class1对象，
// 则该方法返回 true；否则返回 false。
                if(String.class.toString().equals(type)){
                    //获取set方法
                    Method setMethod = o.getClass().getMethod(setMethodName,String.class);
                    //通过invoke获取到get方法的value值
                    String value = (String) getMethod.invoke(o);
                    //通过invoke方法set去空格后的值
                    setMethod.invoke(o,value.trim());
                }

                //判断类型为List<String>类型
                if ("java.util.List<java.lang.String>".equals(type)) {
                    //获取set方法
                    Method setMethod = o.getClass().getMethod(setMethodName, List.class);
                    List<String> value = (List<String>) getMethod.invoke(o);
                    List<String> collect = value.stream().map(String::trim).collect(Collectors.toList());
                    setMethod.invoke(o,collect);
                }
            }
        }
        return proceedingJoinPoint.proceed();
    }
}
