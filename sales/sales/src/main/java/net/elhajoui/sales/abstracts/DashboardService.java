/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.abstracts;

import net.elhajoui.sales.dto.SaleFilterRequestDto;
import net.elhajoui.sales.dto.*;

/**
 *
 * @author marwa
 */
public interface DashboardService {
    
    //counts for total / approved / rejected / pending
    SaleSummaryDto getSalesSammury(Long user_id);
    
    //teams sorted by sales count
    TeamsClassmentDto getTeamRankingCurrentMonth();
    
    
}
