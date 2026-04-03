/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.controllers;

import jakarta.validation.Valid;
import java.io.IOException;
import net.elhajoui.sales.entities.Sale;
import net.elhajoui.sales.services.CustomUserDetails;
import net.elhajoui.sales.services.SaleServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.elhajoui.sales.enums.SaleStatus;

/**
 *
 * @author marwa
 */
@Controller
public class SaleController {
    @Autowired
    private SaleServiceImp saleServiceImp;
    
    //Agent
    
    @GetMapping("/agent/my_sales")
    public String sales(Model model,
                        @RequestParam(name= "keyword", defaultValue = "")String keyword, 
                        @RequestParam(name= "page", defaultValue = "0")int page, 
                        @RequestParam(name= "size", defaultValue = "5") int size){
        
        CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
        
        Long userId = loggedInUser.getId();
    
        model.addAttribute( "saleslist", saleServiceImp.getSalesByAgent(userId,keyword, page, size).getContent() );
        model.addAttribute( "totalPages", new int[saleServiceImp.getSalesByAgent(userId,keyword, page, size).getTotalPages()] );
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "sales/agent/sales";
    }
    
    @GetMapping("/agent/add_sale")
    public String teamAdd(Model model){
        model.addAttribute("sale", new Sale()); 
        return "sales/agent/add_form";
    }
    
    @PostMapping(path = "/sales/save")
    public String saveSale(@Valid Sale sale,@RequestParam("file") MultipartFile file,
                           Authentication auth, BindingResult bindingResult, Model model) throws IOException{
        if(bindingResult.hasErrors()) return "/sales/add_sale";
        saleServiceImp.uploadSale(file, sale.getContractId(), sale.getNote(), auth.getName());
        model.addAttribute("successMessage", "Sale uploaded successfully!");
        return "redirect:/agent/my_sales";
    }
    
    @GetMapping("/agent/my_sale")
    public String showSale(@RequestParam Long saleId, Model model){
        
        CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
        
        Long userId = loggedInUser.getId();
        
        Sale my_sale= saleServiceImp.getSaleByIdAndAgent(userId, saleId);
        
        model.addAttribute("my_sale", my_sale); 
        return "sales/agent/my_sale";
    }
    
    
    @GetMapping("/file/{saleId}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long saleId) throws IOException {
        
         CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
        
        Long userId = loggedInUser.getId();
        
        Sale sale = saleServiceImp.getSaleByIdAndAgent(userId, saleId);
        Path filePath = Paths.get("uploads").resolve(sale.getStoredFile());
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(sale.getContentType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + sale.getOriginalFile() + "\"")
        .header("X-Frame-Options", "SAMEORIGIN") 
        .body(resource);
}
    
    //Manager & admin
    @GetMapping("/sales")
    public String AllSales(Model model,
                        @RequestParam(name= "keyword", defaultValue = "")String keyword, 
                        @RequestParam(name= "page", defaultValue = "0")int page, 
                        @RequestParam(name= "size", defaultValue = "5") int size){
        
        CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
        
        Long userId = loggedInUser.getId();
    
        model.addAttribute( "saleslist", saleServiceImp.getAllSales(userId,keyword, page, size).getContent() );
        model.addAttribute( "totalPages", new int[saleServiceImp.getAllSales(userId,keyword, page, size).getTotalPages()] );
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "sales/manager/sales";
    }
    
    
    @GetMapping("/agent_sale")
    public String showAgentSale(@RequestParam Long saleId, Model model){
        
        CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal();
        
        Long userId = loggedInUser.getId();
        
        Sale my_sale= saleServiceImp.getSaleById(userId, saleId);
        
        model.addAttribute("agent_sale", my_sale); 
        return "sales/manager/agent_sale";
    }
    
    
    
    @PostMapping("/sale_status/update")
    public String updateStatus(
        @RequestParam Long id,
        @RequestParam SaleStatus status,   
        @RequestParam(defaultValue = "") String keyword,
        @RequestParam(defaultValue = "0") int page) {

        CustomUserDetails loggedInUser = (CustomUserDetails) SecurityContextHolder .getContext().getAuthentication().getPrincipal();

        saleServiceImp.updateStatus(loggedInUser.getId(), id, status);
        return "redirect:/sales?page=" + page + "&keyword=" + keyword;
    }
    
}
