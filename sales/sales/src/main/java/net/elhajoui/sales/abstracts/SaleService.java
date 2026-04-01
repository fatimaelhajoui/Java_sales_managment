/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.abstracts;

import net.elhajoui.sales.entities.Sale;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import net.elhajoui.sales.enums.SaleStatus;
import org.springframework.data.domain.Page;
/**
 *
 * @author marwa
 */
public interface SaleService {
    
    //manager & admin

    Page<Sale> getAllSales(Long user_id,String keyword,int page, int size);

    Sale updateStatus(Long user_id,Long id, SaleStatus status);
    
    Sale getSaleById(Long user_id,Long id);
    
    //agent
    Sale uploadSale(MultipartFile file, String contractId, String username) throws IOException;
    
    Page<Sale> getSalesByAgent(Long userId, String keyword,int page, int size);
    
    Sale getSaleByIdAndAgent(Long agent_id, Long id);

    
}
