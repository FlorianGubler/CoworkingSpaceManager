package com.github.floriangubler.coworkspacemgr.service;

import com.github.floriangubler.coworkspacemgr.exception.UserNotFoundException;
import com.github.floriangubler.coworkspacemgr.model.MemberEntity;
import com.github.floriangubler.coworkspacemgr.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
