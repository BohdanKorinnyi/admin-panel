package com.omnia.admin.security;

import com.google.common.collect.ImmutableSet;

import javax.servlet.*;
import java.io.IOException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

//
//@Log4j
//@Component
//@AllArgsConstructor
public class AuthenticationFilter implements Filter {
//    private final TokenService tokenService;
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
//        log.info("Initialization customSecurityRequestFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest servletRequest = (HttpServletRequest) request;
//        if (isSecured(servletRequest.getRequestURI())) {
//            if (tokenService.validate((servletRequest.getCookies()))) {
//                chain.doFilter(servletRequest, response);
//                return;
//            }
//            HttpServletResponse servletResponse = (HttpServletResponse) response;
//            servletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
//            return;
//        }
//        chain.doFilter(servletRequest, response);
//        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
//        log.info("Destroy customSecurityRequestFilter");
    }

//    private boolean isSecured(String path) {
//        for (Predicate<String> predicate : NOT_SECURED_END_POINT) {
//            if (predicate.test(path)) {
//                return false;
//            }
//        }
//        return true;
//    }
}
