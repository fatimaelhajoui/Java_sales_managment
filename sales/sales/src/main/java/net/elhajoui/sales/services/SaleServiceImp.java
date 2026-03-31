/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.services;

import jakarta.validation.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import net.elhajoui.sales.abstracts.SaleService;
import net.elhajoui.sales.entities.AppUser;
import net.elhajoui.sales.entities.Sale;
import net.elhajoui.sales.enums.SaleStatus;
import net.elhajoui.sales.repositories.SaleRepository;
import net.elhajoui.sales.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author marwa
 */
@Service
public class SaleServiceImp implements SaleService{

    @Autowired
    SaleRepository saleRepository;
    
    @Autowired
    UserRepository userRepository;
       
    private final java.nio.file.Path storageLocation = Paths.get("uploads");

    public SaleServiceImp(SaleRepository saleRepository,
                           UserRepository userRepository) throws IOException {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        // Create the uploads folder if it doesn't exist
        Files.createDirectories(storageLocation);
    }
    
    @Override
    public Sale uploadSale(MultipartFile file, String contractId, String username) throws IOException {
          // 1. Validate file is not empty
        if (file.isEmpty()) {
            throw new RuntimeException("Cannot upload an empty file");
        }

        // 2. Sanitize the original filename
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalName.contains("..")) {
            throw new RuntimeException("Invalid filename: " + originalName);
        }

        // 3. Validate file type (only PDF allowed)
        String contentType = file.getContentType();
        List<String> allowedTypes = List.of(
            "application/pdf"
        );
        if (!allowedTypes.contains(contentType)) {
            throw new RuntimeException("File type not allowed: " + contentType);
        }

        // 4. Generate unique stored name to avoid conflicts
        String storedName = UUID.randomUUID() + "_" + originalName;
        java.nio.file.Path targetPath = storageLocation.resolve(storedName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // 5. Find the agent by username
        AppUser agent = userRepository.findOneByUsername(username)
            .orElseThrow(() -> new RuntimeException("Agent not found: " + username));

        // 6. Build and save the Sale entity
        Sale sale = new Sale();
        sale.setOriginalFile(originalName);
        sale.setStoredFile(storedName);
        sale.setContentType(contentType);
        sale.setSize(file.getSize());
        sale.setContractId(contractId);
        sale.setAgent(agent);
        // status defaults to PENDING, uploadedAt set by @PrePersist

        return saleRepository.save(sale);
    }

    @Override
    public Resource downloadFile(Long saleId) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Sale> getAllSales() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Sale getSaleById(Long id) {
        return saleRepository.findById(id).orElseThrow(() -> new RuntimeException("this sale not exist!"));
    }

    @Override
    public Sale updateStatus(Long id, SaleStatus status) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Page<Sale> getSalesByAgent(Long userId, String keyword,int page, int size) {
        
        return saleRepository.findByAgent_IdAndContractIdContaining(userId, keyword, PageRequest.of(page, size));
    }

    @Override
    public void deleteSale(Long id) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Sale getSaleByIdAndAgent(Long agent_id, Long id) {
        return saleRepository.findByAgent_IdAndId(agent_id, id);
    }
    
}
