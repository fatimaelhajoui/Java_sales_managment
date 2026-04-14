package net.elhajoui.sales.abstracts;

import java.util.List;
import net.elhajoui.sales.entities.AppUser;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<AppUser> AllUsers(Long userId, String keyword,int page, int size);
    AppUser createAppUser(Long user_id,AppUser appUser);
    AppUser editAppUser(Long appUser_id);
    AppUser updateAppUser(Long appUser_id,UpdateAppUerDto appUser);
    void deleteAppUser(Long appUser_id);
}
