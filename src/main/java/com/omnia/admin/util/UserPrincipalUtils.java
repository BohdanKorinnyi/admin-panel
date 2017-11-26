package com.omnia.admin.util;

import com.google.common.collect.Lists;
import com.omnia.admin.model.CurrentUser;
import com.omnia.admin.model.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserPrincipalUtils {
    public static int getBuyerId(HttpServletRequest request) {
        if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken userPrincipal = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
            CurrentUser currentUser = (CurrentUser) userPrincipal.getPrincipal();
            return currentUser.getBuyerId();
        }
        throw new RuntimeException();
    }

    public static boolean isRole(HttpServletRequest request, Role role) {
        if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken userPrincipal = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
            CurrentUser currentUser = (CurrentUser) userPrincipal.getPrincipal();
            return Lists.newArrayList(currentUser.getAuthorities()).get(0).getAuthority().equals(role.toString());
        }
        throw new RuntimeException();
    }
}
