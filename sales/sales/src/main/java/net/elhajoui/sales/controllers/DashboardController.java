package net.elhajoui.sales.controllers;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import net.elhajoui.sales.dto.AgentsClassmentDto;
import net.elhajoui.sales.dto.RecentSalesDto;
import net.elhajoui.sales.dto.SaleSummaryDto;
import net.elhajoui.sales.dto.TeamsClassmentDto;
import net.elhajoui.sales.entities.Sale;
import net.elhajoui.sales.services.CustomUserDetails;
import net.elhajoui.sales.services.DashboardServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller 
public class DashboardController {
    
    @Autowired
    private DashboardServiceImp dashboardServiceImp;

    @GetMapping("/dashboard")
    public String dashboard(Model model, RecentSalesDto filter){
        
        CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
        
        Long userId = loggedInUser.getId();
        
        SaleSummaryDto SaleSUM= dashboardServiceImp.getSalesSammury(userId);
        
        List<TeamsClassmentDto> TopTeams = dashboardServiceImp.getTeamRankingCurrentMonth(userId);
        
        List<AgentsClassmentDto> TopAgents= dashboardServiceImp.getAgentRankingCurrentMonth(userId);
        
        Page<Sale> RecentSales= dashboardServiceImp.getRecentSales(userId, filter);
        
        List<Integer> monthlySales = dashboardServiceImp.getMonthlySalesCurrentYear(userId);

        
        model.addAttribute( "Summary", SaleSUM );
        model.addAttribute("TopTeams", TopTeams);
        model.addAttribute("TopAgents", TopAgents);
        model.addAttribute("RecentSales", RecentSales.getContent());
        model.addAttribute("totalPages", new int[RecentSales.getTotalPages()]);
        model.addAttribute("currentPage", filter.getPage());
        
        //chart
        model.addAttribute("monthlySales", monthlySales);

        
        //to hide section by role of user
        model.addAttribute("userRole", loggedInUser.getAuthorities().iterator().next().getAuthority());
        
        return "dashboard/dash";
    }
    
    
    @GetMapping("/dashboard/export")
    public void exportRecentSales(HttpServletResponse response, RecentSalesDto filter) throws IOException {

    CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder
        .getContext().getAuthentication().getPrincipal();

    dashboardServiceImp.exportRecentSalesToExcel(loggedInUser.getId(), filter, response);
    }
}
