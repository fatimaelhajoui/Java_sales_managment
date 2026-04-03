/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.elhajoui.sales.abstracts.UserService;
import net.elhajoui.sales.dto.UpdateAppUerDto;
import net.elhajoui.sales.entities.AppUser;
import net.elhajoui.sales.entities.Team;
import net.elhajoui.sales.repositories.TeamRepository;
import net.elhajoui.sales.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author marwa
 */
@Service
public class UserServiceImp implements UserService {

   @Autowired
   private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TeamRepository teamRepository;

    

    @Override
    public Page<AppUser> AllUsers(Long userId, String keyword,int page, int size) {
        AppUser loggedInUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
        
        if(("ADMIN").equalsIgnoreCase(loggedInUser.getRole())){
            return userRepository.findByUsernameContaining(keyword, PageRequest.of(page, size));
        }else{
           return  userRepository.findByTeamIdAndUsernameContaining(loggedInUser.getTeam().getId(),keyword, PageRequest.of(page, size));
        }
        
    }

    @Override
    public AppUser createAppUser(Long user_id,AppUser appUser) {
            AppUser new_appUser = new AppUser();

    // Check if email already exists
    if (userRepository.existsByMail(appUser.getMail())) {
        throw new RuntimeException("Email '" + appUser.getMail() + "' is already in use");
    }

    // Get the logged-in user 
    AppUser loggedInUser = userRepository.findById(user_id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    // Resolve the target team based on who is logged in
    Team team;
    if ("ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {
        // Admin picks a team from the form — validate it
        team = teamRepository.findById(appUser.getTeam().getId())
            .orElseThrow(() -> new RuntimeException(
                "Team with id " + appUser.getTeam().getId() + " not found"));
    } else {
        // Manager: team is assigned automatically — ignore form input
        team = loggedInUser.getTeam();
    }

    // check manager uniqueness against the resolved team
    if ("MANAGER".equalsIgnoreCase(appUser.getRole())) {
        boolean managerExists = userRepository
            .findByTeamIdAndRole(team.getId(), "MANAGER")
            .isPresent();
        if (managerExists) {
            throw new RuntimeException(
                "Team '" + team.getName() + "' already has a manager. " +
                "A team can only have one manager.");
        }
    }

    new_appUser.setTeam(team);
    new_appUser.setUsername(appUser.getUsername());
    new_appUser.setMail(appUser.getMail());
    new_appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    new_appUser.setRole(appUser.getRole());
    new_appUser.setStatus(appUser.getStatus());

    userRepository.save(new_appUser);
    return new_appUser;
   }

    @Override
    public AppUser editAppUser(Long appUser_id) {
        AppUser my_user= userRepository.findById(appUser_id).orElseThrow(
                ()-> new RuntimeException(
                         "User with id " + appUser_id + " not found"
                )
        );
        
       return my_user;
        
    }

    @Override
    public AppUser updateAppUser(Long appUser_id, UpdateAppUerDto appUser) {
        Team team = null;
                
        AppUser edit_user= userRepository.findById(appUser_id).orElseThrow(
                ()-> new RuntimeException(
                         "User with id " + appUser_id + " not found"
                )
        );

       // Check if email already exists
        if (userRepository.existsByMailAndIdNot(appUser.getMail(), appUser_id)) {
             throw new RuntimeException("Email '" + appUser.getMail() + "' is already in use");
        }

        // Check if team already exists
        team = teamRepository.findById(appUser.getTeam().getId())
        .orElseThrow(() -> new RuntimeException(
            "Team with id " + appUser.getTeam().getId() + " not found"
        ));

       // Check if the updated role is manager and ensure no OTHER user in this team is already manager
        if ("MANAGER".equalsIgnoreCase(appUser.getRole())) {
            boolean anotherManagerExists = userRepository
            .existsByTeamIdAndRoleAndIdNot(team.getId(), "MANAGER", appUser_id);
            if (anotherManagerExists) {
            throw new RuntimeException(
                "Team '" + team.getName() + "' already has a manager. " +
                "A team can only have one manager."
            );
        }
        }

       
        edit_user.setUsername(appUser.getUsername());
        edit_user.setRole(appUser.getRole());
        edit_user.setMail(appUser.getMail());
        edit_user.setTeam(team);
        edit_user.setStatus(appUser.getStatus());
        if (appUser.getPassword() != null && !appUser.getPassword().isBlank()) {
            edit_user.setPassword(passwordEncoder.encode(appUser.getPassword()));
        }
        
        userRepository.save(edit_user);
        
        return edit_user;
        
        
    }

    @Override
    public void deleteAppUser(Long appUser_id) {
        AppUser user_todelete= userRepository.findById(appUser_id).orElse(null);
        if(user_todelete == null){
            throw new RuntimeException("User not exist!");
        } 
        userRepository.deleteById(appUser_id);
    }
    
}
