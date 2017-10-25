package com.omnia.admin.security.service;

import javax.servlet.http.Cookie;

public interface TokenService {

    boolean validate(Cookie[] token);

    String generate(String username, String passwordEncoded);

    String getUsernameFromCookies(Cookie[] cookies);
}
