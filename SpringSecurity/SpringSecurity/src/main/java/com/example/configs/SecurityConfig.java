package com.example.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // .authorizeHttpRequests() set up protected and unprotected URLs
                .authorizeHttpRequests((auth) -> auth

                        // .requestMatchers() set up the NON-PROTECTED URLs.
                        //.permitAll() anyone can see this.
                        .requestMatchers("/V1/index2").permitAll()

                        // .anyRequest() set up the PROTECTED URLs as any other that the last filter doesn't hold.
                        // .authenticated() only authenticated users can see this.
                        .anyRequest().authenticated()
                );

        httpSecurity
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .permitAll()

                        // Redirects to stated URL when Login SUCCESSFUL.
                        .successHandler(successHandler())
                );

        httpSecurity
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        // ALWAYS - will create a session, if there isn't already an existing one.
                        // If there is an existing session already, it will reuse it.
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // ALWAYS - IF_REQUIRED - NEVER - STATELESS
                        .invalidSessionUrl("/login")
                        .maximumSessions(1) // Modify if the application is meant for multiple platforms.
                        .expiredUrl("/login")
                        .sessionRegistry(sessionRegistry())
                );

        httpSecurity
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        // Specific threat protection, when someone tries to fixate a session.
                        .sessionFixation()
                            // Generated another id session when login successful protecting data from threats.
                            .migrateSession() // migrateSession - newSession - none
                );

        //returns a SecurityFilterChain
        return httpSecurity.build();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler(){
        return (((request, response, authentication) -> {
            response.sendRedirect("/V1/session");
        }));
    }

}
