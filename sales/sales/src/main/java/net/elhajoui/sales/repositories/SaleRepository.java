/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.repositories;

import java.util.List;
import net.elhajoui.sales.entities.AppUser;
import net.elhajoui.sales.entities.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author marwa
 */
public interface SaleRepository extends JpaRepository<Sale ,Long>{
    List<Sale> findByAgentUsername(String username);
    Page<Sale> findByAgent_IdAndContractIdContaining(Long user_id, String keyword, Pageable page);
    
    
}
