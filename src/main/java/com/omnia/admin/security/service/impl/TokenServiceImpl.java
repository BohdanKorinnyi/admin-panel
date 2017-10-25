package com.omnia.admin.security.service.impl;

import com.omnia.admin.exception.BadCredentialsException;
import com.omnia.admin.exception.Md5EncodeException;
import com.omnia.admin.model.User;
import com.omnia.admin.security.service.TokenService;
import com.omnia.admin.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ArrayUtils;

import javax.servlet.http.Cookie;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import static com.omnia.admin.security.service.impl.LoginServiceImpl.COOKIE_TOKEN_NAME;

@Log4j
@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private static final Cookie[] EMPTY_COOKIE_ARRAY = new Cookie[0];
    private static final String TOKEN_GENERATE_KEY = "368a16b5874f63cfd14e938cc7b9d0bf";
    private static final String COLON = ":";
    private static final String MD5 = "MD5";

    private final UserService userService;

    @Override
    public boolean validate(Cookie[] cookies) {
        String token = getTokenFromCookies(cookies);
        String tokenUsername = getUsernameFromToken(token);
        Optional<User> user = userService.getUserByName(tokenUsername);
        if (!user.isPresent()) {
            throw new BadCredentialsException();
        }
        User existingUser = user.get();
        String uniqueKey = generateUniqueKey(existingUser.getUsername(), existingUser.getPassword());
        return uniqueKey.equals(getUniqueKeyFromToken(token));
    }

    @Override
    public String generate(String username, String passwordEncoded) {
        String token = username + COLON + generateUniqueKey(username, passwordEncoded);
        return toBase64(token);
    }

    @Override
    public String getUsernameFromCookies(Cookie[] cookies) {
        String token = getTokenFromCookies(cookies);
        return getUsernameFromToken(token);
    }

    private String generateUniqueKey(String username, String passwordEncoded) {
        try {
            String key = username + COLON + passwordEncoded + COLON + TOKEN_GENERATE_KEY;
            MessageDigest md = MessageDigest.getInstance(MD5);
            String uniqueKey = new String(md.digest(key.getBytes()), Charset.forName("UTF-8"));
            return toBase64(uniqueKey);
        } catch (Exception e) {
            throw new Md5EncodeException("Token creation failed for username=" + username);
        }
    }

    private String getTokenFromCookies(Cookie[] cookies) {
        return Arrays.stream(ArrayUtils.isEmpty(cookies) ? EMPTY_COOKIE_ARRAY : cookies)
                .filter(cookie -> COOKIE_TOKEN_NAME.equals(cookie.getName()))
                .map(Cookie::getValue).findFirst()
                .orElseThrow(BadCredentialsException::new);
    }

    private String getUniqueKeyFromToken(String token) {
        String decodedToken = getDecodedToken(token);
        String[] tokenParameters = decodedToken.split(COLON);
        if (tokenParameters.length != 2) {
            log.info("Request refused for token token=" + token);
            throw new BadCredentialsException();
        }
        return tokenParameters[1];
    }

    private String getUsernameFromToken(String token) {
        try {
            String decodeToken = getDecodedToken(token);
            return decodeToken.split(COLON)[0];
        } catch (Exception e) {
            log.error("Error occurred during getting username from token=" + token, e);
            throw new BadCredentialsException();
        }
    }

    private String getDecodedToken(String token) {
        byte[] decodeTokenBytes = Base64.getDecoder().decode(token);
        return new String(decodeTokenBytes);
    }

    private String toBase64(String value) {
        return new String(Base64.getEncoder().encode(value.getBytes()));
    }
}
