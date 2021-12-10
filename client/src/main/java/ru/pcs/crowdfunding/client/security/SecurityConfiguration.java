package ru.pcs.crowdfunding.client.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.pcs.crowdfunding.client.security.filters.TokenAuthorizationFilter;
import ru.pcs.crowdfunding.client.services.ClientsService;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String SIGN_IN_PAGE = "/signIn";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private ClientsService clientsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        TokenAuthorizationFilter tokenAuthorizationFilter =
                new TokenAuthorizationFilter(tokenProvider, objectMapper, clientsService);

        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("**").permitAll()
                .anyRequest().permitAll()
        .and()
        .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID","accessToken")
                .clearAuthentication(true)
        .logoutSuccessUrl(SIGN_IN_PAGE);

        http.addFilterBefore(tokenAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
