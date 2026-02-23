package com.UserAuthenticationService.UserAuthenticationService.config;


import com.UserAuthenticationService.UserAuthenticationService.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain setCustomizedSecurityFilterChain(HttpSecurity http) throws Exception {
        //http.formLogin(Customizer.withDefaults());

        return http.csrf(customizer->customizer.disable())
                .cors(customizer -> customizer.disable()) // Disable CORS as it's handled by Gateway
                .authorizeHttpRequests(request->request
                        //.requestMatchers("register","login").permitAll()//open links
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .requestMatchers("/ws/**")
                        .permitAll()
                        .requestMatchers("/api/v1/admin/**").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/v1/user/**").hasAnyAuthority(Role.CUSTOMER.name())
                        .requestMatchers("/api/v1/owner/**").hasAnyAuthority(Role.RESTAURANT_OWNER.name())
                        .requestMatchers("/api/v1/driver/**").hasAnyAuthority(Role.DELIVERY_DRIVER.name())
                        .anyRequest().authenticated())//remaining all autheication links
                .httpBasic(c->c.disable())//we nee this for rest api access to postman
                //Don’t store authentication info in the HTTP session
                //STATELESS only affects sessions used for authentication
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)//this says before you use UsernamePasswordAuthenticationFilter we ned to check for the jwttoken filter
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration congif) throws Exception {
        return congif.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}
