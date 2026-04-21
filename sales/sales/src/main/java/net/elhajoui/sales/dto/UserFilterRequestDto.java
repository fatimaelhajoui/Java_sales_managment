package net.elhajoui.sales.dto;

import lombok.*;


@Getter @Setter
public class UserFilterRequestDto {
    private String keyword="";
    private Boolean status;
    private String role;
    private int page = 0;
    private int size = 5;
}
