/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.services;

import java.util.List;
import net.elhajoui.sales.abstracts.TeamService;
import net.elhajoui.sales.entities.Team;
import net.elhajoui.sales.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 *
 * @author marwa
 */
@Service
public class TeamServiceImp implements TeamService{
    @Autowired
    private TeamRepository teamRepository;

    @Override
    public Team OneTeam(Long user_id) {
        Team team= teamRepository.findById(user_id).orElse(null);
        if(team == null){
            throw new RuntimeException("Team not exist!");
        }
        return team;
    }

    @Override
    public Page<Team> AllTeams(String keyword, int page, int size) {
        Page<Team> teams= teamRepository.findByNameContaining(keyword, PageRequest.of(page,size));
        return teams;
    }

    @Override
    public Team createTeam(Team team) {
        
        Team new_team = new Team();
        new_team.setName(team.getName());
        new_team.setNote(team.getNote());
        teamRepository.save(new_team);
        
        return new_team;
    }

    @Override
    public Team updateTeam(Long team_id, Team team) {
        Team team_toUpdate= teamRepository.findById(team_id).orElse(null);
        if(team_toUpdate == null){
            throw new RuntimeException("Team not exist!");
        }
        
        team_toUpdate.setName(team.getName());
        team_toUpdate.setNote(team.getNote());
        
        teamRepository.save(team_toUpdate);
        return team_toUpdate;
    }

    @Override
    public Team editTeam(Long team_id) {
        Team team_toUpdate= teamRepository.findById(team_id).orElse(null);
        if(team_toUpdate == null){
            throw new RuntimeException("Team not exist!");
        }
        return team_toUpdate;
    }

    @Override
    public void deleteTeam(Long team_id) {
        Team team_toDelete= teamRepository.findById(team_id).orElse(null);
        if(team_toDelete == null){
            throw new RuntimeException("Team not exist!");
        } 
        teamRepository.deleteById(team_id);
       
    }
    
    
}
