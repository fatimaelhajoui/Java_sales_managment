/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package net.elhajoui.sales.abstracts;

import java.util.List;
import net.elhajoui.sales.entities.Team;
import org.springframework.data.domain.Page;

/**
 *
 * @author marwa
 */

public interface TeamService {
    Team OneTeam(Long user_id);
    Page<Team> AllTeams(String keyword,int page, int size);
    Team createTeam(Team team);
    Team editTeam(Long team_id);
    Team updateTeam(Long team_id,Team team);
    void deleteTeam(Long team_id);
}

