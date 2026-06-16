package com.bpmplatform.security.infrastructure.auth;

import com.bpmplatform.infrastructure.config.JwtService;
import com.bpmplatform.infrastructure.multitenant.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private final JwtService jwtService;
    private final BpmUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, BpmUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        var token = authHeader.substring(7);

        try {
            var email = jwtService.extractEmail(token);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var tenantId = jwtService.extractTenantId(token);
                if (tenantId != null) {
                    TenantContext.setTenantId("tenant_" + tenantId.toString().replace("-", ""));
                }

                var userDetails = userDetailsService.loadUserByUsername(email);
                if (jwtService.validateToken(token) != null) {
                    var authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authenticated user {} with authorities {} for {} {}", email, userDetails.getAuthorities(), request.getMethod(), request.getRequestURI());
                }
            }
        } catch (Exception e) {
            log.warn("Authentication failed for {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        }

        chain.doFilter(request, response);
    }
}
