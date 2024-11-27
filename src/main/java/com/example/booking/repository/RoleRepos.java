package com.example.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking.entity.RoleEntity;


public interface RoleRepos extends JpaRepository<RoleEntity, Long> {
    
}
