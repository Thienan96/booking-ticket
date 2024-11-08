package com.example.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.booking.entity.TicketUserEntity;

public interface TicketUserRepos extends JpaRepository<TicketUserEntity, Long> {

}
