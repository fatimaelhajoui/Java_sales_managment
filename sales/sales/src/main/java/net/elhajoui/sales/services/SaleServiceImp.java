package net.elhajoui.sales.services;

import jakarta.validation.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import net.elhajoui.sales.abstracts.SaleService;
import net.elhajoui.sales.dto.SaleFilterRequestDto;
import net.elhajoui.sales.entities.AppUser;
import net.elhajoui.sales.entities.Sale;
import net.elhajoui.sales.enums.SaleStatus;
import net.elhajoui.sales.repositories.SaleRepository;
import net.elhajoui.sales.repositories.UserRepository;
import net.elhajoui.sales.specification.SaleSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


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
    public Sale uploadSale(MultipartFile file, String contractId, String note, String username) throws IOException {
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
        sale.setNote(note);
        // status defaults to PENDING, uploadedAt set by @PrePersist

        return saleRepository.save(sale);
    }

 
    @Override
    public Page<Sale> getAllSales(Long user_id, SaleFilterRequestDto filter) {
         AppUser loggedInUser = userRepository.findById(user_id)
        .orElseThrow(() -> new RuntimeException("User not found"));
         
         Specification<Sale> spec = Specification
            .where(SaleSpecification.hasStatus(filter.getStatus()))
            .and(SaleSpecification.hasDate(filter.getStartDate(),filter.getEndDate()))
            .and(SaleSpecification.hasKeyword(filter.getKeyword()));
                 
                 
         if(("ADMIN").equalsIgnoreCase(loggedInUser.getRole())){
            
        }
         else  {
            spec = spec.and(SaleSpecification.hasTeam(loggedInUser.getTeam().getId()));
        }
         
         return saleRepository.findAll(spec,PageRequest.of(filter.getPage(), filter.getSize()));
         
    }

    @Override
    public Sale getSaleById(Long user_id, Long id) {
        AppUser loggedInUser = userRepository.findById(user_id)
        .orElseThrow(() -> new RuntimeException("User not found"));
        
         if(("ADMIN").equalsIgnoreCase(loggedInUser.getRole())){
            return saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));
        }else{
           return saleRepository.findByAgent_Team_IdAndId(user_id, id);
        }
         
    }

    @Override
    public Sale updateStatus(Long user_id,Long id, SaleStatus status) {
        AppUser loggedInUser = userRepository.findById(user_id)
        .orElseThrow(() -> new RuntimeException("User not found"));
        
        if(("ADMIN").equalsIgnoreCase(loggedInUser.getRole())){
            Sale agent_sale= saleRepository.findById(id).orElseThrow(() -> new RuntimeException("this sale not exist!"));
        
            agent_sale.setStatus(status);
        
            return saleRepository.save(agent_sale);
        }
        else{
            Sale agent_sale=  saleRepository.findByAgent_Team_IdAndId(user_id, id);
            
            agent_sale.setStatus(status);
        
            return saleRepository.save(agent_sale);
        }
    }

    @Override
    public Page<Sale> getSalesByAgent(Long userId,  SaleFilterRequestDto filter) {
        
        AppUser loggedInUser = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
         
         Specification<Sale> spec = Specification
            .where(SaleSpecification.hasStatus(filter.getStatus()))
            .and(SaleSpecification.hasDate(filter.getStartDate(),filter.getEndDate()))
            .and(SaleSpecification.hasKeyword(filter.getKeyword()));
                 
                 
         if(("AGENT").equalsIgnoreCase(loggedInUser.getRole())){
            spec = spec.and(SaleSpecification.hasAgent(loggedInUser.getId()));
        }
         
         return saleRepository.findAll(spec,PageRequest.of(filter.getPage(), filter.getSize()));
         
        
    }

    @Override
    public Sale getSaleByIdAndAgent(Long user_id, Long id) {
        AppUser loggedInUser = userRepository.findById(user_id)
        .orElseThrow(() -> new RuntimeException("User not found"));
        
        if(("Admin").equalsIgnoreCase(loggedInUser.getRole())){
            
            return saleRepository.findById(id).orElseThrow(() -> new RuntimeException("this sale not exist!"));
        }
        else if(("Manager").equalsIgnoreCase(loggedInUser.getRole())){
            
            return saleRepository.findByAgent_Team_IdAndId(user_id, id);
        }
        else{ //agent role
            
            return saleRepository.findByAgent_IdAndId(user_id, id);
        }
        
        
    }
    
}
