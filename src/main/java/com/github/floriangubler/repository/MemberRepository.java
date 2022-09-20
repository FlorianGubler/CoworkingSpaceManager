package com.github.floriangubler.repository;

import com.github.floriangubler.model.BookingEntity;
import com.github.floriangubler.model.MemberEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends CrudRepository<MemberEntity, UUID> {
    List<MemberEntity> findAll();

    Optional<MemberEntity> findByEmail(String username);

    void deleteById(UUID memberid);
}
