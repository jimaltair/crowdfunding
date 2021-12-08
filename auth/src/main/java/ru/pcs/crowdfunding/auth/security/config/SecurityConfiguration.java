package ru.pcs.crowdfunding.auth.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.pcs.crowdfunding.auth.repositories.AuthenticationInfosRepository;
import ru.pcs.crowdfunding.auth.repositories.AuthorizationInfosRepository;
import ru.pcs.crowdfunding.auth.security.details.AuthenticationInfoDetailsService;
import ru.pcs.crowdfunding.auth.security.filters.TokenAuthenticationFilter;
import ru.pcs.crowdfunding.auth.security.filters.TokenAuthorizationFilter;
import ru.pcs.crowdfunding.auth.security.util.JwtTokenProvider;
import ru.pcs.crowdfunding.auth.security.util.TokenProvider;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    public static final String API = "/api";

    public static final String SIGN_UP_FILTER_PROCESSES_URL = API + "/signUp";
    public static final String SIGN_IN_FILTER_PROCESSES_URL = API + "/signIn";
    public static final String REFRESH_FILTER_PROCESSES_URL = API + "/refresh";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationInfosRepository authenticationInfosRepository;

    @Autowired
    private AuthorizationInfosRepository authorizationInfosRepository;

    @Autowired
    private AuthenticationInfoDetailsService infoDetailsService;

    @Bean
    public TokenProvider tokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(infoDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(authenticationManagerBean(),
                objectMapper, authenticationInfosRepository, authorizationInfosRepository);

        tokenAuthenticationFilter.setFilterProcessesUrl("api/signIn");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("**").hasAuthority("MS_CLIENT");

        http.addFilter(tokenAuthenticationFilter);
        http.addFilterBefore(new TokenAuthorizationFilter(authorizationInfosRepository, objectMapper),
                UsernamePasswordAuthenticationFilter.class);
    }
}
