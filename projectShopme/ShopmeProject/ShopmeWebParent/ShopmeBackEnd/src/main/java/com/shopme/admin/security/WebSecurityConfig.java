package com.shopme.admin.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true,jsr250Enabled = true)
public class WebSecurityConfig {
    @Bean
    UserDetailsService userDetailsService(){
        return new ShopmeUserDetailsService() ;
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authenticationProvider(authenticationProvider());
        httpSecurity.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/**","/settings/**","/countries/**","/states/***").hasAuthority("Admin")
                        .requestMatchers("/categories/**","/brands/**").hasAnyAuthority("Admin","Editor")
                        .requestMatchers("/products/new","products/delete/**").hasAnyAuthority("Admin","Editor")
                        .requestMatchers("/products/edit/**","/products/save","/products/check_unique").hasAnyAuthority("Admin","Editor","Salesperson")
                        .requestMatchers("/products","/products/","/products/detail/**","/products/page/**").hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
                        .requestMatchers("/products/**").hasAnyAuthority("Admin","Editor")
                        .anyRequest().authenticated()
                )
               .formLogin(form->form
                    .loginPage("/login")
                    .usernameParameter("email")
                    .permitAll())
                .logout(logout->logout.permitAll())

                .rememberMe(rem->rem
                            .key("avcdEfghij_123254453")
                            .tokenValiditySeconds(7  * 24 * 60 * 60));
        return httpSecurity.build();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() throws Exception{
        return (web) -> web.ignoring().requestMatchers("/images/**","/js/**","/webjars/**");
    }



}
