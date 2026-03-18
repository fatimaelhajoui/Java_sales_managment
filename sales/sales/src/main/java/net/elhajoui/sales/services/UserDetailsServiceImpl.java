/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.services;

import java.util.Optional;
import net.elhajoui.sales.entities.AppUser;
import net.elhajoui.sales.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author marwa
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    UserRepository userRepository;
            
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> account= userRepository.findOneByUsername(username);
        
        if(account.isEmpty()){
            throw new UsernameNotFoundException("User not found: "+username);
        }
        
        AppUser appUser = account.get();

        //only the active can login
        if(appUser.getStatus()== false){
            throw new UsernameNotFoundException("User is inactive: "+username);
        }
        
        return org.springframework.security.core.userdetails.User
            .withUsername(appUser.getUsername())
            .password(appUser.getPassword())
            .roles(appUser.getRole())
            .build();

    }
    
}

