package com.blogging.service;

import com.blogging.entity.AppUser;
import com.blogging.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AppUserRepository appUserRepository;

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch the AppUser from the database
        AppUser appUser = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with Email: " + email));

        // Map AppUser to Spring Security's UserDetails object
        return User.builder()
                .username(appUser.getEmail())
                .password(appUser.getPassword())  // Password should be already encoded
                .roles(appUser.getRoles().toArray(new String[0]))  // Convert roles to an array of Strings
                .build();
    }
}
