package ru.pcs.crowdfunding.auth.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    public static final String API_PING = "/api/v0/ping";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.formLogin().disable();

        http.authorizeRequests()
                .antMatchers(API_PING).permitAll()
                .anyRequest().permitAll();
    }
}
