package com.github.floriangubler.coworkspacemgr.controller;

import com.github.floriangubler.coworkspacemgr.model.MemberEntity;
import com.github.floriangubler.coworkspacemgr.service.MemberService;
import com.github.floriangubler.coworkspacemgr.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Members", description = "Coworkspace Members management endpoints")
public class MemberController {

    private final MemberService memberService;
    private final static String ADMINROLE = "ROLE_ADMIN";

    MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(
            summary = "Get Members",
            description = "Get all Members (Only Admin)",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @GetMapping("/")
    @PreAuthorize("hasRole(ADMINROLE)")
    List<MemberEntity> loadUserBookings() {
        return memberService.getMembers();
    }

    @Operation(
            summary = "Update a Member",
            description = "Admin updates a Member",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PutMapping("/{memberid}")
    @PreAuthorize("hasRole(ADMINROLE)")
    ResponseEntity<Void> updatemember (
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Member Update", required = true)
            @RequestBody(required = true)
            MemberEntity member,
            @Parameter(description = "MemberID", required = true)
            @RequestParam(name="memberid", required = true)
            UUID memberid) {
        try{
            memberService.update(member, memberid);
        } catch(UserNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Delete a Member",
            description = "Delete a Member (Only Admin)",
            security = {@SecurityRequirement(name = "JWT Auth")}
    )
    @PostMapping("/")
    @PreAuthorize("hasRole(ADMINROLE)")
    ResponseEntity<Void> deletemember(
            @Parameter(description = "MemberID", required = true)
            @RequestParam(name = "memberid", required = true)
            UUID memberid) {
            try{
                memberService.delete(memberid);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch(UserNotFoundException e){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    }

    //Get Roles as String Set
    private Set<String> getRolesSet(Authentication authentication){
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
