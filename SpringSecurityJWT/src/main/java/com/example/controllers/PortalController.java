package com.example.controllers;

import com.example.dtos.CreateClientDTO;
import com.example.models.Client;
import com.example.models.ERole;
import com.example.models.Role;
import com.example.repos.ClientRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PortalController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepo clientRepo;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World NOT SECURED!";
    }

    @GetMapping("/helloSecured")
    public String helloSecured(){
        return "Hello World SECURED!";
    }

    @PostMapping("/createClient")
    public ResponseEntity<?> createClient(@Valid @RequestBody CreateClientDTO createClientDTO){

        Set<Role> roles = createClientDTO.getRoles().stream()
                .map(role -> Role.builder()
                        .name(ERole.valueOf(role))
                        .build())
                .collect(Collectors.toSet());

        Client client = Client.builder()
                .username(createClientDTO.getUsername())
                .password(passwordEncoder.encode(createClientDTO.getPassword())) //password encoded in the db.
                .email(createClientDTO.getEmail())
                .role(roles)
                .build();

        clientRepo.save(client);

        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/deleteClient")
    public String deleteClient(@RequestParam String id){

        clientRepo.deleteById(Long.parseLong(id));

        return "Se ha borrado el cliente con el id ".concat(id);
    }
}
