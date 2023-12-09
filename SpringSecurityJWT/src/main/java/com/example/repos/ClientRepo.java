package com.example.repos;

import com.example.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepo extends CrudRepository<Client, Long> {
    Optional<Client> findByUsername(String username);
//
//    Optional<Client> findByActive(Boolean active);
}
