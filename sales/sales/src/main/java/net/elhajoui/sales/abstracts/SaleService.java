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
    // Agent uploads a file with a contractId
    Sale uploadSale(MultipartFile file, String contractId, String username) throws IOException;

    // Admin downloads a file by sale ID
    Resource downloadFile(Long saleId) throws IOException;

    // Admin lists all sales
    List<Sale> getAllSales();

    // Admin gets a single sale by ID
    Sale getSaleById(Long id);

    // Admin updates status (APPROVED / REJECTED)
      Sale updateStatus(Long id, SaleStatus status);

    // Agent lists only their own sales
    Page<Sale> getSalesByAgent(Long userId, String keyword,int page, int size);

    // Delete a sale and its file from disk
    void deleteSale(Long id) throws IOException;
}
