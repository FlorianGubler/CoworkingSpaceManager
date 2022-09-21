package com.github.floriangubler.coworkspacemgr.repository;

import com.github.floriangubler.coworkspacemgr.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<MemberEntity, UUID> {
    List<MemberEntity> findAll();

    Optional<MemberEntity> findByEmail(String email);

    void deleteById(UUID memberid);
}
