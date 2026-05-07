package net.elhajoui.sales.services;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import net.elhajoui.sales.abstracts.DashboardService;
import net.elhajoui.sales.dto.AgentsClassmentDto;
import net.elhajoui.sales.dto.RecentSalesDto;
import net.elhajoui.sales.dto.SaleFilterRequestDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.jpa.domain.Specification;


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
            summary.setApproved((int) saleRepository.count(SaleSpecification.hasStatus("APPROVED").and(hasCurrentMonth())));
            summary.setPendding((int) saleRepository.count(SaleSpecification.hasStatus("PENDING").and(hasCurrentMonth())));
            summary.setRejected((int) saleRepository.count(SaleSpecification.hasStatus("REJECTED").and(hasCurrentMonth())));
            summary.setTotal(summary.getApproved() + summary.getPendding() + summary.getRejected());
        }
        else if(("MANAGER").equalsIgnoreCase(loggedInUser.getRole())){
            summary.setApproved((int) saleRepository.count(SaleSpecification.hasStatus("APPROVED").and(hasTeam(loggedInUser.getTeam().getId())).and(hasCurrentMonth())));
            summary.setPendding((int) saleRepository.count(SaleSpecification.hasStatus("PENDING").and(hasTeam(loggedInUser.getTeam().getId())).and(hasCurrentMonth())));
            summary.setRejected((int) saleRepository.count(SaleSpecification.hasStatus("REJECTED").and(hasTeam(loggedInUser.getTeam().getId())).and(hasCurrentMonth())));
            summary.setTotal(summary.getApproved() + summary.getPendding() + summary.getRejected());
        }
        else{
            summary.setApproved((int) saleRepository.count(SaleSpecification.hasStatus("APPROVED").and(hasAgent(loggedInUser.getId())).and(hasCurrentMonth())));
            summary.setPendding((int) saleRepository.count(SaleSpecification.hasStatus("PENDING").and((hasAgent(loggedInUser.getId()))).and(hasCurrentMonth())));
            summary.setRejected((int) saleRepository.count(SaleSpecification.hasStatus("REJECTED").and((hasAgent(loggedInUser.getId())).and(hasCurrentMonth()))));
            summary.setTotal(summary.getApproved() + summary.getPendding() + summary.getRejected());
        }
        
        return summary;
        
        
    }

    @Override
    public List<Integer> getMonthlySalesCurrentYear(Long userId) {
        AppUser loggedInUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    int currentYear = LocalDate.now().getYear();
    List<Integer> monthlyCounts = new ArrayList<>();

    for (int month = 1; month <= 12; month++) {
        int finalMonth = month;

        Specification<Sale> spec = hasYear(currentYear)
            .and((root, cb) -> cb.equal(
                cb.function("MONTH", Integer.class, root.get("uploadedAt")), finalMonth
            ));

        if ("ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {
            // no extra filter
        } else if ("MANAGER".equalsIgnoreCase(loggedInUser.getRole())) {
            spec = spec.and(hasTeam(loggedInUser.getTeam().getId()));
        } else {
            spec = spec.and(hasAgent(loggedInUser.getId()));
        }

        monthlyCounts.add((int) saleRepository.count(spec));
    }

    return monthlyCounts;
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
        
        
        
        }else{return List.of();}
        
        return teamsSales; 
        
    }

    @Override
    public List<AgentsClassmentDto> getAgentRankingCurrentMonth(Long user_id) {
         AppUser loggedInUser = userRepository.findById(user_id)
        .orElseThrow(() -> new RuntimeException("User not found"));

        List<AppUser> agents;

        if ("ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {
            agents = userRepository.findAll(); 
        } else if ("MANAGER".equalsIgnoreCase(loggedInUser.getRole())) {
            agents = userRepository.findByTeamId(loggedInUser.getTeam().getId()); 
        } else {
            return List.of();
        }

        List<AgentsClassmentDto> agentSales = new ArrayList<>();

        for (AppUser agent : agents) {
            int count = (int) saleRepository.count(
                SaleSpecification.hasAgent(agent.getId()).and(hasCurrentMonth())
            );
            

            AgentsClassmentDto classmentDto = new AgentsClassmentDto();
            classmentDto.setAgent(agent.getUsername());
            classmentDto.setTeam(agent.getTeamName());
            classmentDto.setSalesCount(count);

        agentSales.add(classmentDto);
        }

        agentSales.sort((a, b) -> b.getSalesCount() - a.getSalesCount());

        return agentSales;
        
    }

    @Override
    public Page<Sale> getRecentSales(Long user_id, RecentSalesDto filter) {
         AppUser loggedInUser = userRepository.findById(user_id)
        .orElseThrow(() -> new RuntimeException("User not found"));
         
         Page<Sale> recentSales;
                 
         if(("ADMIN").equalsIgnoreCase(loggedInUser.getRole())){
            recentSales= saleRepository.findAll(hasToday(),PageRequest.of(filter.getPage(), filter.getSize()));
        }
         else if(("MANAGER").equalsIgnoreCase(loggedInUser.getRole())) {
            recentSales= saleRepository.findAll(hasToday().and(hasTeam(loggedInUser.getTeam().getId())),PageRequest.of(filter.getPage(), filter.getSize()));
        }
         else{
            recentSales= saleRepository.findAll(hasToday().and(hasAgent(loggedInUser.getId())),PageRequest.of(filter.getPage(), filter.getSize()));
        }
         
         return recentSales;
         
    }

    @Override
    public void exportRecentSalesToExcel(Long userId, RecentSalesDto filter, HttpServletResponse response) throws IOException {
        List<Sale> sales = getRecentSales(userId, filter).getContent();

    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setHeader("Content-Disposition", "attachment; filename=recent_sales.xlsx");

    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Recent Sales");

    // Header style
    CellStyle headerStyle = workbook.createCellStyle();
    Font headerFont = workbook.createFont();
    headerFont.setBold(true);
    headerStyle.setFont(headerFont);
    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    // Header row
    Row header = sheet.createRow(0);
    String[] columns = {"#", "Contract ID", "Agent", "Team", "Date", "Status"};
    for (int i = 0; i < columns.length; i++) {
        Cell cell = header.createCell(i);
        cell.setCellValue(columns[i]);
        cell.setCellStyle(headerStyle);
    }

    // Data rows
    int rowNum = 1;
    for (Sale sale : sales) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(rowNum);
        row.createCell(1).setCellValue(sale.getContractId());
        row.createCell(2).setCellValue(sale.getAgent().getUsername());
        row.createCell(3).setCellValue(sale.getAgent().getTeamName());
        row.createCell(4).setCellValue(sale.getUploadedAt().toString());
        row.createCell(5).setCellValue(sale.getStatus().name());
        rowNum++;
    }

    for (int i = 0; i < columns.length; i++) sheet.autoSizeColumn(i);

    workbook.write(response.getOutputStream());
    workbook.close();
    }

    
   
}
