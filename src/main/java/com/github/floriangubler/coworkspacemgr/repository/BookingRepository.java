package com.github.floriangubler.coworkspacemgr.repository;

import com.github.floriangubler.coworkspacemgr.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
    List<BookingEntity> findAll();

    @Query("SELECT b from BOOKING b where b.member.id = :memberId")
    List<BookingEntity> findAllByMemberId(UUID memberId);
}
