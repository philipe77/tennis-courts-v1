package com.tenniscourts.config.security;

import com.tenniscourts.config.persistence.Profile;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/guests").hasRole(String.valueOf(Profile.ADMIN))
                .antMatchers("/guests/*").hasRole(String.valueOf(Profile.ADMIN))
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();
    }

}