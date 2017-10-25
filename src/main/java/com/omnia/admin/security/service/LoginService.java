package com.omnia.admin.security.service;

import com.omnia.admin.dto.LoginDto;
import com.omnia.admin.model.User;

import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    User authenticate(HttpServletResponse response, LoginDto loginDto);
}
