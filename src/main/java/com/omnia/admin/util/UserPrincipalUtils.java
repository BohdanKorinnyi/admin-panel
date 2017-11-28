package com.omnia.admin.util;

import com.google.common.collect.Lists;
import com.omnia.admin.model.CurrentUser;
import com.omnia.admin.model.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserPrincipalUtils {
    private static final int DUMMY_BUYER_ID = -1;

    public static int getBuyerId(HttpServletRequest request) {
        if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken userPrincipal = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
            if (userPrincipal.getPrincipal() instanceof CurrentUser) {
                return (((CurrentUser) userPrincipal.getPrincipal())).getBuyerId();
            }
            return DUMMY_BUYER_ID;
        }
        throw new RuntimeException();
    }

    public static boolean isRole(HttpServletRequest request, Role role) {
        if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken userPrincipal = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
            if (userPrincipal.getPrincipal() instanceof CurrentUser) {
                CurrentUser currentUser = (CurrentUser) userPrincipal.getPrincipal();
                return Lists.newArrayList(currentUser.getAuthorities()).get(0).getAuthority().equals(role.toString());
            }
            return Role.ADMIN.equals(role);
        }
        throw new RuntimeException();
    }

    public static boolean hasRole(HttpServletRequest request, Set<Role> roles) {
        if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken userPrincipal = (UsernamePasswordAuthenticationToken) request.getUserPrincipal();
            if (userPrincipal.getPrincipal() instanceof CurrentUser) {
                CurrentUser currentUser = (CurrentUser) userPrincipal.getPrincipal();
                String role = Lists.newArrayList(currentUser.getAuthorities()).get(0).getAuthority();
                return roles.contains(Role.valueOf(role));
            }
            return false;
        }
        throw new RuntimeException();
    }
}
