package dreipc.asset.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Config
 */

@Configuration
@Profile("!local")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //ToDo: Check Upload and Security Protection

        http
                .authorizeRequests()
                .antMatchers("/graphql", ",/manage/metrics", "/manage/health/**")
                .permitAll();

        http
                .cors();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER);

//                .and()
//                .authorizeRequests()
//                .antMatchers("/api/**");
        http
                .csrf()
                .disable()
                .exceptionHandling();
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/graphql/**").permitAll()
//                .anyRequest().authenticated();
//
//        http
//                .addFilterBefore(jstRequestFilter, UsernamePasswordAuthenticationFilter.class);
//
//        http
//                .exceptionHandling()
//                .authenticationEntryPoint(restSecurityHandler).accessDeniedHandler(restSecurityHandler);
//
//        http.cors();

        return http.build();
    }
}
