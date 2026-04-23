/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author marwa
 */
@Getter @Setter
public class SaleSummaryDto {
    private int total;
    private int approved;
    private int pendding;
    private int rejected;
}
