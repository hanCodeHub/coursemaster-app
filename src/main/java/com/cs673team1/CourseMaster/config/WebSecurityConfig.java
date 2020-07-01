package com.cs673team1.CourseMaster.config;

import com.cs673team1.CourseMaster.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Security config class with boilerplate code for customizing authentication.
 *
 * @see <a href="https://spring.io/guides/tutorials/spring-boot-oauth2/">
 *     Spring Boot OAuth2</a>
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Creates logger for security configuration.
     */
    private final Logger logger = LoggerFactory
            .getLogger(WebSecurityConfig.class);

    /**
     * Autowires UserService class to this class.
     */
    @Autowired
    UserService userService;

    /**
     * Defines which endpoints are allowed and which ones are secured.
     *
     * @param http HttpSecurity object with authentication config methods
     * @throws Exception HTTP Exception caught when client communiates with
     * server
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();

        // configures allowed CORS origins
        http.cors().configurationSource(request -> {
            var corsConfig = new CorsConfiguration();
            corsConfig.addAllowedOrigin("http://localhost:3000");
            return corsConfig;
        });

        http
            .authorizeRequests(a -> a
                    .antMatchers("/", "/error", "/images/**", "/h2-console/**")
                    .permitAll()
                    .anyRequest().authenticated()
            )
            .exceptionHandling(e -> e
                    .authenticationEntryPoint(
                            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .csrf(c -> c
                    .csrfTokenRepository(CookieCsrfTokenRepository
                    .withHttpOnlyFalse())
                    .ignoringAntMatchers("/h2-console/**")
            )
            // TODO: localhost:3000 used for development only, change to relative path in prod
            .oauth2Login().defaultSuccessUrl("http://localhost:3000/home", true)
            .and()
            .logout()
                .logoutSuccessUrl("http://localhost:3000/") // development only
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                .permitAll();
    }

    /**
     * Handles user information when successfully logged in to system.
     *
     * @param authSuccess Indicates successful authentication so user
     *                    information can be managed
     */
    @EventListener
    public void handleAuthSuccess(AuthenticationSuccessEvent authSuccess) {
        // logs current user with details from Authentication object
        OAuth2User principal = (OAuth2User) authSuccess.getAuthentication()
                .getPrincipal();
        String name = (String) principal.getAttributes().get("name");
        String email = (String) principal.getAttributes().get("email");
        logger.info("User logged in: name: " + name + ", email: " + email);

        // delegate account creation or update to UserService
        if (!userService.userExists(principal)) {
            userService.createOAuth2User(principal);
        } else {
            userService.updateOAuth2User(principal);
        }
    }


}
