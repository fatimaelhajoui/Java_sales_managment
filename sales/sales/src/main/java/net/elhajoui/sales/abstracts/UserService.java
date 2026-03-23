/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.elhajoui.sales.abstracts;

import java.util.List;
import net.elhajoui.sales.entities.AppUser;
import org.springframework.data.domain.Page;

/**
 *
 * @author marwa
 */
public interface UserService {
    Page<AppUser> AllUsers(Long userId, String keyword,int page, int size);
    AppUser createAppUser(AppUser appUser);
    AppUser editAppUser(Long appUser_id);
    AppUser updateAppUser(Long appUser_id,AppUser appUser);
    void deleteAppUser(Long appUser_id);
}
