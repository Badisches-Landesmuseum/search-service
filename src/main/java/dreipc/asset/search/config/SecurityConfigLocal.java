package dreipc.asset.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Config enables the Spring Security with JWT.
 */

@Configuration
@Profile("local") // Allow all endpoints on local development
public class SecurityConfigLocal {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Allow everything
        http
                .authorizeRequests()
                .anyRequest()
                .permitAll();

        // Security Settings
        http
                .csrf()
                .disable()
                .exceptionHandling()
                .and()
                .cors()
                .disable();

        return http.build();
    }
}
