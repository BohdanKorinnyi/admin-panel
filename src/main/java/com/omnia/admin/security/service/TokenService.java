package com.omnia.admin.security.service;

import com.omnia.admin.dto.LoginDto;

public interface TokenService {
    String generate(LoginDto loginDto);
}
