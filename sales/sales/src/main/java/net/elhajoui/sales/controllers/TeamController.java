package net.elhajoui.sales.controllers;

import jakarta.validation.Valid;
import net.elhajoui.sales.entities.Team;
import net.elhajoui.sales.services.TeamServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TeamController {
    @Autowired
    private TeamServiceImp teamServiceImp;
    
    
    @GetMapping("/teams")
    public String teams( Model model, 
                        @RequestParam(name= "keyword", defaultValue = "")String keyword,
                        @RequestParam(name= "page", defaultValue = "0")int page, 
                        @RequestParam(name= "size", defaultValue = "5") int size)
    {
        
      model.addAttribute("teamslist", teamServiceImp.AllTeams(keyword,page,size).getContent());
      model.addAttribute("totalPages", new int[teamServiceImp.AllTeams(keyword,page,size).getTotalPages()]);
      model.addAttribute("currentPage", page);
      model.addAttribute("keyword", keyword);
        return "teams/teams";
    } 
    
    
    @GetMapping("/teams/add_form")
    public String teamAdd(Model model){
        model.addAttribute("team", new Team()); 
        return "teams/add_form";
    }
    
    @PostMapping(path = "/teams/save")
    public String saveTeam(@Valid Team team, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()) return "teams/add_form";
        teamServiceImp.createTeam(team);
        return "redirect:/teams";
    }
    
    @GetMapping("/teams/edit_form")
    public String teamEdit(@RequestParam Long team_id, Model model,
                        @RequestParam(name= "keyword", defaultValue = "")String keyword,
                        @RequestParam(name= "page", defaultValue = "0")int page, 
                        @RequestParam(name= "size", defaultValue = "5") int size) {
        model.addAttribute("team", teamServiceImp.editTeam(team_id));
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "teams/edit_form";
    }
    
    @PostMapping("/teams/update")
    public String updateTeam(@Valid Team team, BindingResult bindingResult, Model model,
                        @RequestParam(name= "keyword", defaultValue = "")String keyword,
                        @RequestParam(name= "page", defaultValue = "0")int page, 
                        @RequestParam(name= "size", defaultValue = "5") int size) {
        if (bindingResult.hasErrors()) return "teams/edit_form";
        teamServiceImp.updateTeam(team.getId(), team);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "redirect:/teams?page="+ page +"&keyword="+ keyword;
    }
    
   @PostMapping("/teams/delete")
    public String teamDelete(@RequestParam Long team_id) {
       teamServiceImp.deleteTeam(team_id);
        return "redirect:/teams";
    }
    
}
