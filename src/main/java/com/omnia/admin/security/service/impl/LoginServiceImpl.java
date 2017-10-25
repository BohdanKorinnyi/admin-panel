package com.omnia.admin.security.service.impl;

import com.omnia.admin.dto.LoginDto;
import com.omnia.admin.exception.BadCredentialsException;
import com.omnia.admin.exception.UserNotFoundException;
import com.omnia.admin.model.User;
import com.omnia.admin.security.service.LoginService;
import com.omnia.admin.security.service.TokenService;
import com.omnia.admin.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Log4j
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    public static final String COOKIE_TOKEN_NAME = "token";

    private final UserService userService;
    private final TokenService tokenService;

    @Override
    public User authenticate(HttpServletResponse response, LoginDto loginDto) {
        Optional<User> user = userService.getUserByName(loginDto.getUsername());
        if (user.isPresent()) {
            log.info("User " + loginDto.getUsername() + " has been found");
            if (BCrypt.checkpw(loginDto.getPassword(), user.get().getPassword())) {
                response.addCookie(new Cookie(COOKIE_TOKEN_NAME, tokenService.generate(loginDto.getUsername(), user.get().getPassword())));
                return user.get();
            }
        }
        throw new BadCredentialsException();
    }
}
