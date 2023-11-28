package com.cbo.sso.security;


import com.cbo.sso.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Inject the JwtUtils and UserDetailsServiceImpl beans
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get the authorization header from the request
        String header = request.getHeader("Authorization");
//        System.out.println(header);


        // Check if the header is valid and starts with "Bearer "
        if (header != null && header.startsWith("Bearer ")) {
            // Get the token from the header
            String token = header.substring(7);
//            System.out.println("token");

            // Validate the token
            if (jwtUtils.validateToken(token)) {
                // Get the username from the token
                String username = jwtUtils.getUsernameFromToken(token);

                // Get the user details from the userDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create an authentication object from the user details and token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication to the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}

