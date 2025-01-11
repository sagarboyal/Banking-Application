package com.main.application.security;

import com.main.application.ImplementationService.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor // Generates a constructor with parameters for all final fields
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider; // Utility class to handle JWT operations
    private CustomUserDetailsService userDetailsService; // Service to load user details

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // Extract token from the Authorization header
            String token = getTokenFromRequest(request);

            // Validate the token and check if it's present
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                // Extract username from the token
                String username = jwtTokenProvider.getUserName(token);

                // Load user details from the database or other persistent storage
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create an authentication object with the user's authorities (roles/permissions)
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, // Principal (user details)
                                null, // Credentials (not needed for JWT)
                                userDetails.getAuthorities()); // Authorities (roles/permissions)

                // Add additional details about the request
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication object in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            // Log the exception for debugging or auditing purposes
            System.err.println("Invalid token: " + e.getMessage());
        }

        // Continue the filter chain to the next filter or endpoint
        filterChain.doFilter(request, response);
    }

    // Utility method to extract the JWT token from the Authorization header
    private String getTokenFromRequest(@NotNull HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); // Get the Authorization header
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Extract token after "Bearer "
        }
        return null; // Return null if the token is missing or invalid
    }
}
