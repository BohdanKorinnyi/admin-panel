package com.omnia.admin.security;

import com.google.common.collect.ImmutableSet;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Log4j
@Component
public class CustomSecurityRequestFilter implements Filter {

    private static final Set<Predicate<String>> NOT_SECURED_END_POINT = ImmutableSet.of(
            Pattern.compile("/swagger-ui.html").asPredicate(),
            Pattern.compile("/swagger-resources/configuration/ui").asPredicate(),
            Pattern.compile("/swagger-resources").asPredicate(),
            Pattern.compile("/v2/api-docs").asPredicate(),
            Pattern.compile("/webjars/*").asPredicate(),
            Pattern.compile("/login").asPredicate()
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Initialization customSecurityRequestFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (isSecured(servletRequest.getRequestURI())) {
            log.info(servletRequest.getRequestURI() + " secured");
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            servletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        log.info(servletRequest.getRequestURI() + " skip auth");
        chain.doFilter(servletRequest, response);
    }

    @Override
    public void destroy() {
        log.info("Destroy customSecurityRequestFilter");
    }

    private boolean isSecured(String path) {
        for (Predicate<String> predicate : NOT_SECURED_END_POINT) {
            if (predicate.test(path)) {
                return false;
            }
        }
        return true;
    }
}
