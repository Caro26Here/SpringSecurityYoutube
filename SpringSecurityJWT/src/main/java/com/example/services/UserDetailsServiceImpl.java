package com.example.services;

import com.example.models.Client;
import com.example.repos.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientRepo clientRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The client " + username + " doesn't exist."));

        Collection<? extends GrantedAuthority> authorities = client.getRole()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name() ) ) )
                .collect(Collectors.toSet());

        return new User(client.getUsername(),
                client.getPassword(),
                true,
                true,
                true,
                true,
                authorities);
    }
}
