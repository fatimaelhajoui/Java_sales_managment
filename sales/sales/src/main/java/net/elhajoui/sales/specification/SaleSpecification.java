package net.elhajoui.sales.specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import net.elhajoui.sales.entities.Sale;
import net.elhajoui.sales.enums.SaleStatus;
import org.springframework.data.jpa.domain.Specification;


public class SaleSpecification {
    public static Specification<Sale> hasStatus(String status) {
        return (root, query, cb) -> {
        if (status == null || status.trim().isEmpty()) return null; 
        try {
            return cb.equal(root.get("status"), SaleStatus.valueOf(status.trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return null; 
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
    
    //current month statistic
    public static Specification<Sale> hasCurrentMonth() {
    return (root, query, cb) -> {
        LocalDate now = LocalDate.now();
        LocalDateTime start = now.withDayOfMonth(1).atStartOfDay();
        LocalDateTime end = now.withDayOfMonth(now.lengthOfMonth()).atTime(23, 59, 59);
        return cb.between(root.get("uploadedAt"), start, end);
    };    
    }
    
    //current day sales
    public static Specification<Sale> hasToday() {
    return (root, query, cb) -> {
        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        return cb.between(root.get("uploadedAt"), start, end);
    };
    }
    
    //current year staistics
    public static Specification<Sale> hasYear(int year) {
        return (root, query, cb) -> cb.equal(
        cb.function("YEAR", Integer.class, root.get("uploadedAt")), year
    );
    }
    
}
