package com.omnia.admin.security.service.impl;

import com.omnia.admin.model.CurrentUser;
import com.omnia.admin.model.User;
import com.omnia.admin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public CurrentUser loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByName(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new CurrentUser(user);
    }
}
