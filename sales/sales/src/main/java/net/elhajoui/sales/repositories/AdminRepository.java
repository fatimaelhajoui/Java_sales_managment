/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.repositories;

import java.util.Optional;
import net.elhajoui.sales.entities.AppAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author marwa
 */

public interface AdminRepository extends JpaRepository<AppAdmin ,Long>{
     Optional<AppAdmin> findOneByUsername(String username);
}
