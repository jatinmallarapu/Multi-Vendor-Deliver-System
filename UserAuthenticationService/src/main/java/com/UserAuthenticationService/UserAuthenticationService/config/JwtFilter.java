package com.UserAuthenticationService.UserAuthenticationService.config;

import com.UserAuthenticationService.UserAuthenticationService.service.JWTService;
import com.UserAuthenticationService.UserAuthenticationService.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the token from the http request
        String authHeader = request.getHeader("Authorization");// this will get the comeplete token (with Bearer in it).
        String token = null;
        String username = null;

        // if token is not empty and should start with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);// skipping Bearer part in the token and getting the subtring into the token
                                            // variable
            username = jwtService.extractUserName(token);
        }
        // SecurityContextHolder.getContext(): Security Context acts as a storage box
        // for authentication info (like “who is logged in”).
        // So == null means:
        // "No user has been authenticated for this request yet — let’s authenticate the
        // token now."
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Get the user data from the database on what username[extracted username from
            // the token] you are passing
            // user information from DB: Contains username, password (hashed), roles, etc.
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

            // And verify it is matching or not
            /*
             * Checks two things:
             * 1)Does the username in the token match the DB?
             * 2)Is the token expired or still valid?
             */
            if (jwtService.validateToken(token, userDetails)) {
                // if it is matching we have to create a new authentication object and set that
                // in the context as we are telling the next filter that we have the
                // authentication
                // once the token is valid we should make the next filter work

                // This creates an authentication object that Spring Security uses to represent
                // a logged-in user.
                // “Hey Spring, here’s an authenticated user, created from a valid token.”
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                // Attach details about the request, This adds extra info like: IP address,
                // Session ID, User agent (browser info)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // So, by doing this we add the token in the security chain
                // Put authentication in context
                // It tells Spring Security: “This request is authenticated. You can trust this
                // user for the rest of the request lifecycle.”
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }
        // Let the request continue next filter in the SecurityFilterChain
        filterChain.doFilter(request, response);

    }
}
