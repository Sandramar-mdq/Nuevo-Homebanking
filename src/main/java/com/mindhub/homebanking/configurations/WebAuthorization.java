package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity //habilita la config de  la seguridad
@Configuration
public class WebAuthorization {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                /*
                .antMatchers("/web/index.html", "/web/index.js", "/web/css/**", "/web/img/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login", "/api/logout", "/api/clients").permitAll()
                .antMatchers("/rest/**", "/rest/loans", "/rest/cards").hasAuthority("ADMIN")
                .antMatchers("/h2-console").hasAuthority("ADMIN")
                .antMatchers( "/api/accounts", "/api/accounts/{id}","/api/loan",
                        "/api/clients", "/api/clients/{id}", "/api/clients/current", "/api/cards",
                        "/api/cards/{id}", "/api/transactions", "/api/transactions/{id}").hasAuthority("CLIENT");
                //.anyRequest().denyAll();

                 */

                .antMatchers(HttpMethod.POST,"/api/clients", "/api/login").permitAll()
                .antMatchers( "/web/index.html", "/api/clients/current/accounts").permitAll()
                //.antMatchers(HttpMethod.POST, ).permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers( "/api/**", "/api/clients/current", "/client/current/cards",
                        "/api/transactions", "/client/current/transactions").hasAuthority("CLIENT");


        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login"); //post method


        http.logout().logoutUrl("/api/logout").deleteCookies("JSESSIONIG");


        // turn off checking for CSRF tokens
        http.csrf().disable();

        //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());


        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

    }

}
