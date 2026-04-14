/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.dto;

import lombok.*;

/**
 *
 * @author marwa
 */
@Getter @Setter
public class UserFilterRequestDto {
    private String keyword="";
    private Boolean status;
    private String role;
    private int page = 0;
    private int size = 5;
}
