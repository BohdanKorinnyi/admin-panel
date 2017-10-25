package com.omnia.admin.security.service.impl;

import com.omnia.admin.dto.LoginDto;
import com.omnia.admin.exception.BadCredentialsException;
import com.omnia.admin.exception.Md5EncodeException;
import com.omnia.admin.security.service.TokenService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;

@Log4j
@Service
public class TokenServiceImpl implements TokenService {

    private static final String TOKEN_GENERATE_KEY = "368a16b5874f63cfd14e938cc7b9d0bf";
    private static final String COLON = ":";
    private static final String MD5 = "MD5";

    @Override
    public String generate(LoginDto loginDto) {
        String token = loginDto.getUsername() + COLON + toBase64(generateMd5Key(loginDto));
        return toBase64(token);
    }

    @Override
    public String getUsernameFromToken(String token) {
        try {
            byte[] decodeTokenBytes = Base64.getDecoder().decode(token);
            String decodeToken = new String(decodeTokenBytes);
            return decodeToken.split(COLON)[0];
        } catch (Exception e) {
            log.error("Error occurred during getting username from token=" + token, e);
            throw new BadCredentialsException();
        }
    }

    private String generateMd5Key(LoginDto loginDto) {
        try {
            String key = loginDto.getUsername() + COLON + loginDto.getPassword() + COLON + TOKEN_GENERATE_KEY;
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
