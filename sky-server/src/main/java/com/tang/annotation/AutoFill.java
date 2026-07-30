package com.tang.annotation;


import com.tang.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解：用于标识某个方法需要进行公共字段的自动填充
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {

    //

    /**
     * 定义注解的一个属性:数据库的操作类型
     使用@AutoFill(value = OperationType.INSERT)，可以省略value =
     * @return
     */
    OperationType value();

}
