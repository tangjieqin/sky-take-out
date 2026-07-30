package com.tang.aspect;

import com.tang.BaseContext;
import com.tang.annotation.AutoFill;
import com.tang.constant.AutoFillConstant;
import com.tang.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段的自动填充功能
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     *切入点:拦截的是mapper方法中且加了@AutoFill注解的方法
     */
    @Pointcut("execution(* com.tang.mapper.*.*(..)) && @annotation(com.tang.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * 前置通知:在方法执行前调用
     */
    @Before("autoFillPointCut()")
    public void beforeFill(JoinPoint joinPoint) throws NoSuchMethodException {
        log.info("自动填充公共字段");
        // 从joinPoint中获取方法参数: 获取方法的签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 从方法签名对象获取方法上的注解
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        // 获取注解中的操作类型
        OperationType operationType = autoFill.value();

        // 获取目标方法的参数（mapper）
        Object[] args = joinPoint.getArgs();

        if (args == null || args.length == 0) {
            return;
        }

        // 实体对象
        Object entity = args[0];

        // 准备赋值
        LocalDateTime time = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 插入操作：设置创建时间、更新时间、创建人、修改人
        if (operationType == OperationType.INSERT) {
            try {
                // 获取set方法对象——Method
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射调用目标对象的方法
                setCreateTime.invoke(entity, time);
                setUpdateTime.invoke(entity, time);
                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("自动填充插入公共字段失败: {}", e.getMessage());
            }
        } else if (operationType == OperationType.UPDATE) {
            // 执行Update操作：设置更新时间、修改人
            try {
                // 获取set方法对象——Method
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射调用目标对象的方法
                setUpdateTime.invoke(entity, time);
                setUpdateUser.invoke(entity, currentId);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("自动填充更新公共字段失败: {}", e.getMessage());
            }
        }
    }
}
