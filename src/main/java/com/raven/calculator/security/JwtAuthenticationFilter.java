package com.raven.calculator.security;

import com.raven.calculator.dto.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, 
                                 CustomUserDetailsService userDetailsService,
                                 ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/") || 
               path.startsWith("/swagger-ui/") || 
               path.startsWith("/v3/api-docs/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain chain)
            throws ServletException, IOException {
        String header = req.getHeader("Authorization");
        String token = (header != null && header.startsWith("Bearer "))
                ? header.substring(7)
                : null;

        try {
            if (token == null) {
                sendErrorResponse(res, "Token is required");
                return;
            }

            if (!jwtUtil.validateToken(token)) {
                sendErrorResponse(res, "Token is invalid or expired");
                return;
            }
            
            String username = jwtUtil.getUsernameFromToken(token);
            UserDetails ud = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(auth);
            
            chain.doFilter(req, res);
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            sendErrorResponse(res, "Token is invalid or expired");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        
        ErrorResponse error = new ErrorResponse(
            HttpServletResponse.SC_UNAUTHORIZED,
            "Unauthorized",
            message
        );
        
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}