package com.ms.sw.config;

import com.ms.sw.entity.User;
import com.ms.sw.service.JwtService;
import com.ms.sw.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = null;

        // 1. Attempt to extract token from the standard Authorization header (REST)
        final String authHeader = request.getHeader("Authorization");

        // The log shows this is null for the WS handshake
        log.info("authHeader: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            log.debug("Extracted JWT from Header.");
        }



        // 2. 💡 CRITICAL FIX: If token is null AND this is the WS path, check query parameter
        if (jwt == null && request.getRequestURI().equals("/server-status")) {
            jwt = request.getParameter("token");

            if (jwt != null) {
                log.debug("Extracted JWT from Query Parameter for WS.");
            }
        }

        // 3. If token is STILL null after checking both header and query param, proceed without authentication
        if (jwt == null) {
            log.debug("No JWT found in header or query param. Proceeding unauthenticated.");
            filterChain.doFilter(request, response);
            return;
        }


        String username;

        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            log.error("ERROR: JWT validation failed.", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid token");
            return;
        }


        Optional<User> user = userService.getUser(username);
        if (user.isEmpty()) {
            log.error("ERROR: User not found for username: {}", username);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not found");
            return;
        }
        User u = user.get();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(u, null,null);

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        log.info("Successfully authenticated user: {}", username);
        filterChain.doFilter(request, response);
    }
}
