/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales;

import java.util.Optional;
import net.elhajoui.sales.entities.AppUser;
import net.elhajoui.sales.repositories.UserRepository;
import net.elhajoui.sales.services.UserServiceImp;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTests {
    @Mock
    UserRepository userRepository;
    
    @InjectMocks
    UserServiceImp userService;
    
    @Test
    void NonDoubleEmail(){
        AppUser loggeInAdmin = new AppUser();
        loggeInAdmin.setId(1L);
        loggeInAdmin.setRole("ADMIN");
        
        AppUser newUser = new AppUser();
        newUser.setMail("taken@mail.com");
        
        when(userRepository.existsByMail("taken@mail.com")).thenReturn(true);
        
        RuntimeException ex= assertThrows(RuntimeException.class, () -> userService.createAppUser(1L, newUser));
        
        assertTrue(ex.getMessage().contains("already in use"));
    }
    
}
