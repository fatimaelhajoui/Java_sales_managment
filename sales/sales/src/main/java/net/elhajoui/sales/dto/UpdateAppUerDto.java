package net.elhajoui.sales.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import net.elhajoui.sales.entities.Team;

@Getter @Setter
public class UpdateAppUerDto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotEmpty(message = "Full name is required")
    @Size(min = 2, max = 100, message = "username must be between 2 and 100 characters")
    private String username;
    
    @Size(min = 4, max = 255, message = "password must be between 4 and 255 characters")
    private String password;
    
    
    @NotEmpty(message = "email is required")
    @Column(name = "email", nullable = false, unique = true)
    @Email(message="invalid email format")
    private String mail;
    
    
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
