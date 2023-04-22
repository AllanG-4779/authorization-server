package com.allang.authorizationserver.security;

import com.allang.authorizationserver.entity.AppUser;
import com.allang.authorizationserver.repository.AppUserRepository;
import com.allang.authorizationserver.repository.AuthorizationRepository;
import com.allang.authorizationserver.repository.ClientRepository;
import com.allang.authorizationserver.security.service.CustomUserDetailsService;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {
    @Autowired
    private  AppUserRepository appUserRepository;
    @Autowired RegisteredClientRepository registeredClientRepository;
    @Autowired
    public AuthorizationRepository authorizationRepository;


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults()); // Enable OIDC support 1.0
        httpSecurity.exceptionHandling(exception -> exception.authenticationEntryPoint((
                        new LoginUrlAuthenticationEntryPoint("/login")
                )))
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/register","login", "/register-client/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin().disable().csrf().disable();
        return httpSecurity.build();
    }
 @Bean
 AuthenticationProvider authenticationProvider(){
     DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
     authenticationProvider.setPasswordEncoder(passwordEncoder());
     authenticationProvider.setUserDetailsService(userDetailsService());
     return authenticationProvider;
 }

    @Bean
    @Order(3)
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(appUserRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Persist registered client to database

//    @Bean
//
//    public OAuth2AuthorizationService authorizationService(){
//        return new JpaOAuth2AuthorizationService(authorizationRepository, registeredClientRepository);
//    }
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }
    @Bean
    public AppUser appUser(){
        return new AppUser();
    }
}