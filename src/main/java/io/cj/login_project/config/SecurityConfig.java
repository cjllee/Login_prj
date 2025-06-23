package io.cj.login_project.config;

import io.cj.login_project.service.CustomOAuth2UserService;
import io.cj.login_project.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/login", "/signup", "/send-verification-code").permitAll()
                .requestMatchers("/welcome").hasRole("ADMIN") // Only ROLE_ADMIN can access /welcome
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/welcome", true)
                .permitAll()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/welcome", true)
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .and()
                .logout()
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .csrf()
                .disable()
                .userDetailsService(customUserDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}