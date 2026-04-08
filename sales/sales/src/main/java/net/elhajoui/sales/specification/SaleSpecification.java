/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.specification;

import java.util.Date;
import net.elhajoui.sales.entities.Sale;
import net.elhajoui.sales.enums.SaleStatus;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author marwa
 */
public class SaleSpecification {
    public static Specification<Sale> hasStatus(String status) {
        return (root, query, cb) -> {
        if (status == null || status.trim().isEmpty()) return null; // ✅ handles "" and null
        try {
            return cb.equal(root.get("status"), SaleStatus.valueOf(status.trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return null; // ✅ safety net for invalid values
        }
    };

    }

    public static Specification<Sale> hasDate(Date startDate, Date endDate) {
        return (root, query, cb) ->
            startDate == null ? null : cb.and(
                    cb.greaterThanOrEqualTo(root.get("uploadedAt"), startDate),
                    cb.lessThanOrEqualTo(root.get("uploadedAt"), endDate));
    }
    
    
    // this for keyword
    public static Specification<Sale> hasKeyword(String keyword) {
        return (root, query, cb) ->
            keyword == null ? null : cb.like(root.get("contractId"), "%" + keyword + "%");
    }

    // this for team (manager)
    public static Specification<Sale> hasTeam(Long teamId) {
        return (root, query, cb) ->
            teamId == null ? null : cb.equal(root.get("agent").get("team").get("id"), teamId);
    }

    // this for agent
    public static Specification<Sale> hasAgent(Long agentId) {
        return (root, query, cb) ->
            agentId == null ? null : cb.equal(root.get("agent").get("id"), agentId);
    }
    
}
