package com.self.study.flashsale.flashsale.drivers.db.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import com.self.study.flashsale.flashsale.drivers.db.entities.EventsEntity;

import jakarta.persistence.LockModeType;

public interface EventsRepository extends JpaRepository<EventsEntity, UUID> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT e FROM EventsEntity e WHERE e.id = :id")
    Optional<EventsEntity> findByIdForUpdate(UUID id);
}
