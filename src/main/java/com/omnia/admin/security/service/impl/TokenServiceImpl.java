package com.omnia.admin.security.service.impl;

import com.omnia.admin.dto.LoginDto;
import com.omnia.admin.exception.Md5EncodeException;
import com.omnia.admin.security.service.TokenService;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;

@Service
public class TokenServiceImpl implements TokenService {

    private static final String TOKEN_GENERATE_KEY = "368a16b5874f63cfd14e938cc7b9d0bf";
    private static final String COLUMN = ":";
    private static final String MD5 = "MD5";

    @Override
    public String generate(LoginDto loginDto) {
        String token = loginDto.getUsername() + COLUMN + toBase64(generateMd5Key(loginDto));
        return toBase64(token);
    }

    private String generateMd5Key(LoginDto loginDto) {
        try {
            String key = loginDto.getUsername() + COLUMN + loginDto.getPassword() + COLUMN + TOKEN_GENERATE_KEY;
            MessageDigest md = MessageDigest.getInstance(MD5);
            return new String(md.digest(key.getBytes()), Charset.forName("UTF-8"));
        } catch (Exception e) {
            throw new Md5EncodeException("Token creation failed for username=" + loginDto.getUsername());
        }
    }

    private String toBase64(String value) {
        return new String(Base64.getEncoder().encode(value.getBytes()));
    }
}
