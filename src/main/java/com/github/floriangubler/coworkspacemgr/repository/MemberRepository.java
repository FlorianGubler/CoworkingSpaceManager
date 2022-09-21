package com.github.floriangubler.coworkspacemgr.repository;

import com.github.floriangubler.coworkspacemgr.model.MemberEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends CrudRepository<MemberEntity, UUID> {
    List<MemberEntity> findAll();

    Optional<MemberEntity> findByEmail(String username);

    void deleteById(UUID memberid);
}
