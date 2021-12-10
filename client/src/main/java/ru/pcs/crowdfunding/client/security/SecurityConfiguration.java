package ru.pcs.crowdfunding.client.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    public static final String API_PING = "/api/v0/ping";
    public static final String SIGN_IN_PAGE = "/signIn";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers(API_PING).permitAll()
                .anyRequest().permitAll()
        .and()
        .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID","accessToken")
                .clearAuthentication(true)
        .logoutSuccessUrl(SIGN_IN_PAGE);
    }
}
