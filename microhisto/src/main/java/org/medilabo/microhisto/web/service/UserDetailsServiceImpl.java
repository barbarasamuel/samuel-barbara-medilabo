package org.medilabo.microhisto.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    /*@Autowired*/
    private final UserDetails userDetails;


    public UserDetailsServiceImpl() {
        // Création d’un utilisateur en mémoire
        this.userDetails = User.builder()
                .username("currentUser")
                .password("{noop}")
                .authorities("USER")
                .build();
    }

    /**/@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (!"currentUser".equals(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return userDetails;
    }

}
