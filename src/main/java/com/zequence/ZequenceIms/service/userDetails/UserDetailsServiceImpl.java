package com.zequence.ZequenceIms.service.userDetails;


import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;
import com.zequence.ZequenceIms.exceptions.NotFoundException;
import com.zequence.ZequenceIms.repository.AuthenticationRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthenticationRepository repository;


    public UserDetailsServiceImpl(AuthenticationRepository repository) {
        this.repository = repository;
    }
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        UserAuthenticationDetails user = repository.findByEmail(identifier)
                .orElseGet(() -> repository.findByUsername(identifier)
                        .orElseThrow(() -> new NotFoundException(String.format("User does not exist, identifier: %s", identifier))));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>()) // Add user roles/authorities here later
                .build();
    }
}
