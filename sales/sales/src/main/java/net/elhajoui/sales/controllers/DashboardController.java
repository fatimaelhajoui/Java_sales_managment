/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.controllers;

import net.elhajoui.sales.dto.SaleSummaryDto;
import net.elhajoui.sales.services.CustomUserDetails;
import net.elhajoui.sales.services.DashboardServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author marwa
 */

@Controller 
public class DashboardController {
    
    @Autowired
    private DashboardServiceImp dashboardServiceImp;

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        
        CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
        
        Long userId = loggedInUser.getId();
        
        SaleSummaryDto SaleSUM= dashboardServiceImp.getSalesSammury(userId);
        
        model.addAttribute( "Summary", SaleSUM );
        
        return "dashboard/dash";
    }
}
