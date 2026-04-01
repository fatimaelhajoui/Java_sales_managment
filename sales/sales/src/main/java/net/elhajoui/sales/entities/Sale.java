/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.elhajoui.sales.enums.SaleStatus;

/**
 *
 * @author marwa
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFile;
    private String storedFile;
    private String contentType;       
    private long size;

    @NotEmpty(message = "Contract Id is required")
    private String contractId;

    @Enumerated(EnumType.STRING)       //stored as "PENDING" in DB
    private SaleStatus status = SaleStatus.PENDING;

    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "agent_id", nullable = false)
    private AppUser agent;

    @PrePersist
    public void prePersist() {
        this.uploadedAt = LocalDateTime.now();  //auto-set on save
    }

    public String getAgentName() {
        return agent.getUsername();
    }
}
