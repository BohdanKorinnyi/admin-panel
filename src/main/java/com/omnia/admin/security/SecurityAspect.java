package com.omnia.admin.security;

import com.omnia.admin.model.Role;
import com.omnia.admin.security.annotation.RequiredRole;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.lang.reflect.Method;

@Log4j
@Aspect
@Component
@AllArgsConstructor
public class SecurityAspect {

    @Before(value = "@annotation(com.omnia.admin.security.annotation.RequiredRole)")
    public Object checkRole(JoinPoint joinPoint) {
        Role[] requiredRoles = getRequiredRoles(joinPoint);
        Object[] args = joinPoint.getArgs();
        return args[0];
    }


    private Role[] getRequiredRoles(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(RequiredRole.class).roles();
    }

    private String getUsernameFromCookies(Cookie[] cookies) {


        return "";
    }
}
