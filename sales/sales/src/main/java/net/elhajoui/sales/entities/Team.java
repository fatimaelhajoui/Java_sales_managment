/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 *
 * @author marwa
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString 
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotEmpty(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Team name must be between 2 and 100 characters")
    private String name;
    
    private String note;
}
