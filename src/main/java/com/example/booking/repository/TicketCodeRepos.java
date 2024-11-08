package com.example.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking.entity.TicketCodeEntity;

public interface TicketCodeRepos extends JpaRepository<TicketCodeEntity, Long> {

}
