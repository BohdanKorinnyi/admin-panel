package com.omnia.admin.security;

import com.google.common.collect.Sets;
import com.omnia.admin.exception.ForbiddenResourceException;
import com.omnia.admin.model.Role;
import com.omnia.admin.model.User;
import com.omnia.admin.security.annotation.RequiredRole;
import com.omnia.admin.security.service.TokenService;
import com.omnia.admin.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;
//@Log4j
//@Aspect
//@Component
//@AllArgsConstructor
public class AuthorizationService {

//    private final UserService userService;
//    private final TokenService tokenService;

//    @Before(value = "@annotation(com.omnia.admin.security.annotation.RequiredRole)")
    public void checkRole(JoinPoint joinPoint) {
//        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
//        long start = System.currentTimeMillis();
//        Role[] requiredRoles = getRequiredRoles(joinPoint);
//        String username = tokenService.getUsernameFromCookies(request.getCookies());
//        Optional<User> userByName = userService.getUserByName(username);
//        if (userByName.isPresent() && Sets.newHashSet(requiredRoles).contains(Role.parse(userByName.get().getRoleId()))) {
//            log.info("User authorization has taken " + (System.currentTimeMillis() - start) + " ms");
//            return;
//        }
//        log.info("User authorization has taken " + (System.currentTimeMillis() - start) + " ms");
//        throw new ForbiddenResourceException();
    }

//    private Role[] getRequiredRoles(JoinPoint joinPoint) {
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//        return method.getAnnotation(RequiredRole.class).roles();
//    }
}
