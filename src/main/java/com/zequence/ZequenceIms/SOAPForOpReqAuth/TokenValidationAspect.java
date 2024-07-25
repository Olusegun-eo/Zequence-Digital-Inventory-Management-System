package com.zequence.ZequenceIms.SOAPForOpReqAuth;


import com.zequence.ZequenceIms.exceptions.AccessDeniedException;
import com.zequence.ZequenceIms.helper.JwtHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TokenValidationAspect {
    private final JwtHelper jwtHelper;

    private final HttpServletRequest request;

    private final UserDetailsService userDetailsService;

    @Before("@annotation(com.zequence.ZequenceIms.RequireAuthentication)")
//    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
//            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
//            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
//            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void beforeMethodExecution() {
        String token = jwtHelper.extractTokenFromHeader(request);
        if (token == null || jwtHelper.isTokenBlacklisted(token)) {
            throw new AccessDeniedException("Token is invalid or blacklisted");
        }

        String username = jwtHelper.extractUsername(token);
        if (username == null) {
            throw new AccessDeniedException("Token is invalid");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtHelper.validateToken(token, userDetails)) {
            throw new AccessDeniedException("Token is invalid or expired");
        }
    }
}