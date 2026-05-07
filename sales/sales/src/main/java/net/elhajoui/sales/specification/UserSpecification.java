/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.specification;

import net.elhajoui.sales.entities.AppUser;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author marwa
 */
public class UserSpecification {
     // this for status
    public static Specification<AppUser> hasStatus(Boolean status) {
        return (root, query, cb) -> {
        if (status == null) return null; 
        try {
            return cb.equal(root.get("status"), status);
        } catch (IllegalArgumentException e) {
            return null; 
        }
    };

    }
    
     // this for filter by role
    public static Specification<AppUser> hasRole(String role) {
        return (root, query, cb) -> {
        if (role == null || role.trim().isEmpty()) return null; 
        try {
            return cb.equal(root.get("role"), role);
        } catch (IllegalArgumentException e) {
            return null; 
        }
    };
     }          
    
     // get users by manager team id 
    public static Specification<AppUser> hasTeam(Long teamId) {
        return (root, query, cb) ->
            teamId == null ? null : cb.equal(root.get("team").get("id"), teamId);
    }

     // this for keyword
    public static Specification<AppUser> hasKeyword(String keyword) {
        return (root, query, cb) ->
            keyword == null ? null : cb.like(root.get("username"), "%" + keyword + "%");
    }

}
