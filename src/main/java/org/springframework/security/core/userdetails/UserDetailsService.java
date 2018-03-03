package org.springframework.security.core.userdetails;

import com.tracker.config.security.authentification.CustomUserObject;
import org.bson.types.ObjectId;

public interface UserDetailsService {
    void reloadUsers();
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    CustomUserObject loadUserById(ObjectId id) throws UsernameNotFoundException;
    CustomUserObject loadUserDataByUsername(String username) throws UsernameNotFoundException;
    ObjectId loadUserIdByUsername(String username) throws UsernameNotFoundException;
}
