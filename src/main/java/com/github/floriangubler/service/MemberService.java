package com.github.floriangubler.service;

import com.github.floriangubler.exception.BookingNotFoundException;
import com.github.floriangubler.exception.UserNotFoundException;
import com.github.floriangubler.model.BookingEntity;
import com.github.floriangubler.model.MemberEntity;
import com.github.floriangubler.repository.BookingRepository;
import com.github.floriangubler.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MemberService {

    private final MemberRepository repository;

    MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public List<MemberEntity> getMembers(){
        log.info("Executing find all Members ...");
        return repository.findAll();
    }

    public MemberEntity update(MemberEntity member, UUID memberid){
        if(repository.findById(memberid).isPresent()){
            log.info("Executing update user with id " + memberid + " ...");
            member.setId(memberid);
            return repository.save(member);
        } else{
            throw new UserNotFoundException("Member with given id not found");
        }
    }

    public void delete(UUID memberid){
        if(repository.findById(memberid).isPresent()){
            log.info("Executing delete user with id " + memberid + " ...");
            repository.deleteById(memberid);
        } else{
            throw new UserNotFoundException("Member with given id not found");
        }
    }

}
