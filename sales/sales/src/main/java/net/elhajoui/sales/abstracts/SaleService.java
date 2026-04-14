
package net.elhajoui.sales.abstracts;

import net.elhajoui.sales.entities.Sale;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;
import net.elhajoui.sales.enums.SaleStatus;
import org.springframework.data.domain.Page;

public interface SaleService {
    
    //manager & admin
    Page<Sale> getAllSales(Long user_id,SaleFilterRequestDto filter);

    Sale updateStatus(Long user_id,Long id, SaleStatus status);
    
    Sale getSaleById(Long user_id,Long id);
    
    //agent
    Sale uploadSale(MultipartFile file, String contractId, String note, String username) throws IOException;
    
    Page<Sale> getSalesByAgent(Long userId,  SaleFilterRequestDto filter);
    
    Sale getSaleByIdAndAgent(Long user_id, Long id);
}
