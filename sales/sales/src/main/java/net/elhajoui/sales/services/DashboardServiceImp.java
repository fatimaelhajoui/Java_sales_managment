
package net.elhajoui.sales.services;

import java.util.ArrayList;
import java.util.List;
import net.elhajoui.sales.abstracts.DashboardService;
import net.elhajoui.sales.dto.SaleSummaryDto;
import net.elhajoui.sales.dto.TeamsClassmentDto;
import net.elhajoui.sales.entities.AppUser;
import net.elhajoui.sales.entities.Sale;
import net.elhajoui.sales.entities.Team;
import net.elhajoui.sales.repositories.SaleRepository;
import net.elhajoui.sales.repositories.TeamRepository;
import net.elhajoui.sales.repositories.UserRepository;
import net.elhajoui.sales.specification.SaleSpecification;
import static net.elhajoui.sales.specification.SaleSpecification.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;



@Service
public class DashboardServiceImp implements DashboardService{
    
    
    @Autowired
    SaleRepository saleRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    TeamRepository teamRepository;

    @Override
    public SaleSummaryDto getSalesSammury(Long user_id) {
        
        AppUser loggedInUser = userRepository.findById(user_id)
        .orElseThrow(() -> new RuntimeException("User not found"));
        
        SaleSummaryDto summary = new SaleSummaryDto();
        
        
        if(("ADMIN").equalsIgnoreCase(loggedInUser.getRole())){
            summary.setApproved((int) saleRepository.count(SaleSpecification.hasStatus("APPROVED")));
            summary.setPendding((int) saleRepository.count(SaleSpecification.hasStatus("PENDING")));
            summary.setRejected((int) saleRepository.count(SaleSpecification.hasStatus("REJECTED")));
            summary.setTotal(summary.getApproved() + summary.getPendding() + summary.getRejected());
        }
        else if(("MANAGER").equalsIgnoreCase(loggedInUser.getRole())){
            summary.setApproved((int) saleRepository.count(SaleSpecification.hasStatus("APPROVED").and(hasTeam(loggedInUser.getTeam().getId()))));
            summary.setPendding((int) saleRepository.count(SaleSpecification.hasStatus("PENDING").and(hasTeam(loggedInUser.getTeam().getId()))));
            summary.setRejected((int) saleRepository.count(SaleSpecification.hasStatus("REJECTED").and(hasTeam(loggedInUser.getTeam().getId()))));
            summary.setTotal(summary.getApproved() + summary.getPendding() + summary.getRejected());
        }
        else{
            summary.setApproved((int) saleRepository.count(SaleSpecification.hasStatus("APPROVED").and(hasAgent(loggedInUser.getId()))));
            summary.setPendding((int) saleRepository.count(SaleSpecification.hasStatus("PENDING").and((hasAgent(loggedInUser.getId())))));
            summary.setRejected((int) saleRepository.count(SaleSpecification.hasStatus("REJECTED").and((hasAgent(loggedInUser.getId())))));
            summary.setTotal(summary.getApproved() + summary.getPendding() + summary.getRejected());
        }
        
        return summary;
        
        
    }

    @Override
    public List<TeamsClassmentDto> getTeamRankingCurrentMonth(Long user_id) {
        
        AppUser loggedInUser = userRepository.findById(user_id)
        .orElseThrow(() -> new RuntimeException("User not found"));
        
        
        
        List<Team> teams = teamRepository.findAll();
        List<TeamsClassmentDto> teamsSales = new ArrayList<>();
        
        if(("ADMIN").equalsIgnoreCase(loggedInUser.getRole())){
        
        for(Team team: teams){
            int count = (int) saleRepository.count(SaleSpecification.hasTeam(team.getId())
                        .and(hasCurrentMonth()));
            
            TeamsClassmentDto classmentDto = new TeamsClassmentDto();
            classmentDto.setSalesCount(count);
            classmentDto.setTeam(team.getName());
            
            teamsSales.add(classmentDto);
        }
        
        teamsSales.sort((a, b) -> b.getSalesCount() - a.getSalesCount());
        
        
        
        }
        return teamsSales; 
        
    }

   
}
