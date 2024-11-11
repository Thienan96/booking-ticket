package com.example.booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.example.booking.entity.TicketEntity;

import jakarta.persistence.LockModeType;

import java.util.List;


public interface TicketRepos extends JpaRepository<TicketEntity, Long> {

    // @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TicketEntity> findById(Long id);
}
