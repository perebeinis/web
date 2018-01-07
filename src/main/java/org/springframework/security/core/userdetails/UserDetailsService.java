package org.springframework.security.core.userdetails;

import com.tracker.config.security.authentification.CustomUserObject;

public interface UserDetailsService {
    void reloadUsers();
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    CustomUserObject loadUserDataByUsername(String username) throws UsernameNotFoundException;
}
