/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.controllers;

import jakarta.validation.Valid;
import net.elhajoui.sales.abstracts.UserService;
import net.elhajoui.sales.entities.AppUser;
import net.elhajoui.sales.repositories.TeamRepository;
import net.elhajoui.sales.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author marwa
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @GetMapping("/users")
    public String index(Model model,
                        @RequestParam(name= "keyword", defaultValue = "")String keyword, 
                        @RequestParam(name= "page", defaultValue = "0")int page, 
                        @RequestParam(name= "size", defaultValue = "5") int size){
        model.addAttribute( "userslist", userService.AllUsers(keyword, page, size).getContent() );
        model.addAttribute( "totalPages", new int[userService.AllUsers(keyword, page, size).getTotalPages()] );
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "users/users";
    }
    
    @GetMapping("/users/add_form")
    public String appUserAdd(Model model){
         model.addAttribute("appUser", new AppUser()); 
         model.addAttribute("teams", teamRepository.findAll()); 
        return "users/add_form";
    } 
    
    @PostMapping(path = "/users/save")
    public String saveUser (@Valid AppUser appUser, BindingResult bindingResult, Model model){
        model.addAttribute("teams", teamRepository.findAll());
        if (bindingResult.hasErrors()) return "users/add_form";
        try {
            userService.createAppUser(appUser);
        }catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("teams", teamRepository.findAll());
            return "users/add_form";
        }
        
        return "redirect:/users";

    }
    
    @GetMapping("/users/edit_form")
    public String AppUserEdit(@RequestParam Long user_id, Model model,
                        @RequestParam(name= "keyword", defaultValue = "")String keyword,
                        @RequestParam(name= "page", defaultValue = "0")int page, 
                        @RequestParam(name= "size", defaultValue = "5") int size) {
        model.addAttribute("my_user", userService.editAppUser(user_id));
        model.addAttribute("teams", teamRepository.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "users/edit_form";
    }
    
    
    
}
