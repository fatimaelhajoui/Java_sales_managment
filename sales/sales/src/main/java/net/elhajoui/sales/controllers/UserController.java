package net.elhajoui.sales.controllers;

import jakarta.validation.Valid;
import java.util.List;
import net.elhajoui.sales.abstracts.UserService;
import net.elhajoui.sales.dto.UpdateAppUerDto;
import net.elhajoui.sales.dto.UserFilterRequestDto;
import net.elhajoui.sales.entities.AppUser;
import net.elhajoui.sales.repositories.TeamRepository;
import net.elhajoui.sales.repositories.UserRepository;
import net.elhajoui.sales.services.CustomUserDetails;
import net.elhajoui.sales.specification.UserSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @GetMapping("/users")
    public String index(Model model,UserFilterRequestDto filter){
        
        CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
        
        Long userId = loggedInUser.getId();
        
        Page<AppUser>  userPage = userService.AllUsers(userId, filter);
    
        model.addAttribute( "userslist", userPage.getContent() );
        model.addAttribute( "totalPages", new int[userPage.getTotalPages()] );
        model.addAttribute("currentPage", filter.getPage());
        model.addAttribute("keyword", filter.getKeyword());
        model.addAttribute("filter", filter);
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
            CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = loggedInUser.getId();
            userService.createAppUser(userId,appUser);
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
    
     @PostMapping("/users/update")
    public String updateAppUser(@Valid UpdateAppUerDto updateAppUerDto, BindingResult bindingResult, Model model,
                        @RequestParam(name= "keyword", defaultValue = "")String keyword,
                        @RequestParam(name= "page", defaultValue = "0")int page, 
                        @RequestParam(name= "size", defaultValue = "5") int size,
                        @RequestParam(name= "teamId", required = false) Long teamId) {

        // Manually bind team
        if (teamId != null) {
            net.elhajoui.sales.entities.Team t = new net.elhajoui.sales.entities.Team();
            t.setId(teamId);
            updateAppUerDto.setTeam(t);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("my_user", updateAppUerDto);
            model.addAttribute("teams", teamRepository.findAll());
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", keyword);
            return "users/edit_form";
        }

        try {
            userService.updateAppUser(updateAppUerDto.getId(), updateAppUerDto);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("my_user", updateAppUerDto);
            model.addAttribute("teams", teamRepository.findAll());
            model.addAttribute("currentPage", page);
            model.addAttribute("keyword", keyword);
            return "users/edit_form";
        }

        return "redirect:/users?page=" + page + "&keyword=" + keyword;
    }
    
    
    @PostMapping("/users/delete")
    public String userDelete(@RequestParam Long user_id) {
       userService.deleteAppUser(user_id);
        return "redirect:/users";
    }
    
}
