package com.example.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.booking.entity.ClientEntity;


public interface ClientRepos extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByEmail(@Param("email") String email);
}
