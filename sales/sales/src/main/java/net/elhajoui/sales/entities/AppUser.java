/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 *
 * @author marwa
 */
@Entity(name = "app_user")
@Table(name = "app_user")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString 
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotEmpty(message = "Full name is required")
    @Size(min = 2, max = 100, message = "username must be between 2 and 100 characters")
    private String username;
    
    @NotEmpty(message = "password is required")
    @Size(min = 4, max = 255, message = "password must be between 4 and 255 characters")
    private String password;
    
    
    @NotEmpty(message = "email is required")
    @Column(name = "email", nullable = false, unique = true)
    @Email(message="invalid email format")
    private String mail;
    
    @NotEmpty(message = "role is required")
    private String role;
    
    @Column(name = "status", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean status = false;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team ;
    
    public String getTeamName(){
        return team.getName();
    }
}

