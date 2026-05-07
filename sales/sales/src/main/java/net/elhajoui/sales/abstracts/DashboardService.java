package net.elhajoui.sales.abstracts;

import java.util.List;
import net.elhajoui.sales.dto.SaleFilterRequestDto;
import net.elhajoui.sales.dto.*;

public interface DashboardService {
    
    //counts for total / approved / rejected / pending
    SaleSummaryDto getSalesSammury(Long user_id);
    
    //teams sorted by sales count
   List<TeamsClassmentDto> getTeamRankingCurrentMonth(Long user_id);
    
    //teams sorted by sales count
    List<TeamsClassmentDto> getTeamRankingCurrentMonth(Long user_id);
   
   //agent sorted by sales count
   List<AgentsClassmentDto> getAgentRankingCurrentMonth(Long user_id);
   
   //todays sales 
   Page<Sale> getRecentSales(Long user_id, RecentSalesDto filter);
    
    
}
