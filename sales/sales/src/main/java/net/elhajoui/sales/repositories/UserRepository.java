/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.repositories;

import java.util.Optional;
import net.elhajoui.sales.entities.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author marwa
 */
public interface UserRepository extends JpaRepository<AppUser,Long>{
    Page<AppUser> findByTeamIdAndUsernameContaining(Long team_id, String keyword, Pageable page);
    Page<AppUser> findByUsernameContaining(String keyword, Pageable page);
    Optional<AppUser> findOneByUsername(String username);
    boolean existsByMail(String mail);
    boolean existsByMailAndIdNot(String mail, Long id); //Only block if ANOTHER user has that email in edit_form
    Optional<AppUser> findByTeamIdAndRole(Long teamId, String role); // Find existing manager in a team, optionally excluding a specific user (for update)
    boolean existsByTeamIdAndRoleAndIdNot(Long teamId, String role, Long excludeId); // For update — exclude the current user being edited

}
