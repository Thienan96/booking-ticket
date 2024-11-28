package com.example.booking.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.booking.entity.KeyStoreEntity;


public interface KeyStoreRepos extends JpaRepository<KeyStoreEntity, UUID> {
    @Modifying
    @Query(value = "DELETE FROM KeyStoreEntity keyStoreEntity " +
                   " WHERE keyStoreEntity.id = :uuid ")
    void deleteById(@Param("uuid") UUID uuid);

    @Modifying
    @Query(value = "DELETE FROM KeyStoreEntity keyStoreEntity " +
                   " WHERE keyStoreEntity.userName = :userName ")
    void deleteByUserName(@Param("userName") String userName);
}
