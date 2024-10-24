package com.example.application.Config;

import com.example.application.Enums.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    final
    JwtFilter   jwtFilter ;
    final
    UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable).
            authorizeHttpRequests(requests ->
                    {
                        requests.requestMatchers("api/auth/adduser").permitAll();
                        requests.requestMatchers("api/auth/addAdmin").permitAll();
                        requests.requestMatchers("api/updateamount").hasAnyAuthority(Roles.ADMIN.name());
                        requests.requestMatchers("CardPayment/{cName}/addamount/{monthYear}").hasAnyAuthority(Roles.ADMIN.name());
                        requests.requestMatchers("api/cards/createcard").hasAnyAuthority(Roles.ADMIN.name());
                        requests.requestMatchers("api/cards/updateCard/{cName}").hasAnyAuthority(Roles.ADMIN.name());
                        requests.requestMatchers("api/auth/user/login").permitAll();
                        requests.requestMatchers("api/auth/{userId}/deluser").hasAnyAuthority(Roles.ADMIN.name());
                        requests.requestMatchers("mail/verify/{email}").permitAll();
                        requests.requestMatchers("mail/valid/{otp}/{username}").permitAll();
                        requests.requestMatchers("alive").permitAll();
                     requests.anyRequest().authenticated();})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider (){
        DaoAuthenticationProvider daoAuthenticationProvider  = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return daoAuthenticationProvider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration  configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
