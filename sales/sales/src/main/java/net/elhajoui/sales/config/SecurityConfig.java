/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author marwa
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
   
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> {
             auth.requestMatchers("/login/**").permitAll()
                //for table Team    
                .requestMatchers(HttpMethod.GET, "/teams/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/teams/**").hasRole("ADMIN")
                //for table AppUser 
                .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers(HttpMethod.POST, "/users/**").hasAnyRole("ADMIN", "MANAGER")
                //for table Sale 
                .requestMatchers(HttpMethod.GET, "/sales/add_sale").hasRole("AGENT")
                .requestMatchers(HttpMethod.POST, "/sales/save").hasRole("AGENT")
                
                .anyRequest().authenticated();
                
            })
            .formLogin(form -> form
                .loginPage("/login")              
                .defaultSuccessUrl("/users", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/login?expired=true") // Handle expired sessions
             );

            return http.build();
    }
}
