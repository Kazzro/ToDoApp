package de.inmediasp.TodoApp.security;

import de.inmediasp.TodoApp.service.UserDetailsServiceImpl;
import de.inmediasp.TodoApp.service.jwt.AuthEntryPointJwt;
import de.inmediasp.TodoApp.service.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@Import(CorsConfig.class)
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @ConditionalOnProperty(name = "de.inmediasp.TodoApp.app.localEnvironment", havingValue = "true")
    @Bean
    public SecurityFilterChain filterChainCorsDisabled(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        configureAuthorization(http, true,  "api/users/**", "api/user/**", "/api/test");
        return http.build();
    }


    @ConditionalOnProperty(name = "de.inmediasp.TodoApp.app.localEnvironment", havingValue = "false")
    @Bean
    public SecurityFilterChain filterChainCorsEnabled(HttpSecurity http) throws Exception {
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        configureAuthorization(http, false,  "api/users/**", "api/user/**");
        return http.build();
    }

    private void configureAuthorization(HttpSecurity http, boolean h2, String... patterns) throws Exception {
        if(h2){
            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/**").permitAll()
                    .requestMatchers( "/h2-console/**").permitAll()
                    .requestMatchers(patterns).permitAll().anyRequest().authenticated());
        } else {
            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/**").permitAll()
                    .requestMatchers(patterns).permitAll().anyRequest().authenticated());
        }
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
