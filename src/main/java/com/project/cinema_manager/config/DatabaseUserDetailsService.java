package com.project.cinema_manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.cinema_manager.model.User;
import com.project.cinema_manager.repository.UserRepository;
import java.util.Optional;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Seguendo le slide: usiamo Optional e verifichiamo la presenza
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return new DatabaseUserDetails(user.get());
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }
}