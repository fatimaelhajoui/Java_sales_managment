/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.controllers;

import net.elhajoui.sales.entities.Sale;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author marwa
 */
@Controller
public class SaleController {
    @GetMapping("/sales/add_sale")
    public String teamAdd(Model model){
        model.addAttribute("sale", new Sale()); 
        return "sales/add_form";
    }
}
