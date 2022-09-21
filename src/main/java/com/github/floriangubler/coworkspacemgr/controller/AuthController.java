package com.github.floriangubler.coworkspacemgr.controller;

import com.github.floriangubler.coworkspacemgr.entity.RegisterDTO;
import com.github.floriangubler.coworkspacemgr.exception.UserAlreadyExistsException;
import com.github.floriangubler.coworkspacemgr.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import com.github.floriangubler.coworkspacemgr.entity.MemberEntity;
import com.github.floriangubler.coworkspacemgr.entity.TokenResponse;
import com.github.floriangubler.coworkspacemgr.repository.MemberRepository;
import com.github.floriangubler.coworkspacemgr.security.JwtServiceHMAC;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtServiceHMAC jwtService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Operation(
            summary = "Get new token",
            operationId = "getToken",
            tags = {"Authorization"}
    )
    @PostMapping(value = "/token", produces = "application/json")
    public TokenResponse getToken(
            @Parameter(
                    description = "The grant type which will be used to get an new token",
                    required = true,
                    schema = @Schema(allowableValues = {"password", "refresh_token"})
            )
            @RequestParam(name = "grant_type", required = true)
            String grantType,
            @Parameter(description = "If refresh_token is selected as grant type this field is needed")
            @RequestParam(name = "refresh_token", required = false)
            String refreshToken,
            @Parameter(description = "If password is selected as grant type this field is needed", required = false)
            @RequestParam(name = "email", required = false)
            String email,
            @Parameter(description = "If password is selected as grant type this field is needed", required = false)
            @RequestParam(name = "password", required = false)
            String password) throws GeneralSecurityException, IOException {

        switch (grantType) {
            case "password" -> {
                val optionalMember = memberRepository.findByEmail(email);
                if (optionalMember.isEmpty()) {
                    throw new IllegalArgumentException("Username or password wrong");
                }

                if (!BCrypt.checkpw(password, optionalMember.get().getPasswordHash())) {
                    throw new IllegalArgumentException("Username or password wrong");
                }

                val member = optionalMember.get();

                val id = UUID.randomUUID().toString();
                val scopes = new ArrayList<String>();

                if (member.getIsAdmin()) {
                    scopes.add("ADMIN");
                }

                val newAccessToken = jwtService.createNewJWT(id, member.getId().toString(), member.getEmail(), scopes);
                val newRefreshToken = jwtService.createNewJWTRefresh(id, member.getId().toString());

                return new TokenResponse(newAccessToken, newRefreshToken, "Bearer", LocalDateTime.now().plusDays(14).toEpochSecond(ZoneOffset.UTC), LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC));
            }
            case "refresh_token" -> {
                val jwt = jwtService.verifyJwt(refreshToken, false);

                val optionalMember = memberRepository.findById(UUID.fromString(jwt.getClaim("user_id").asString()));
                if (optionalMember.isEmpty()) {
                    throw new IllegalArgumentException("Invalid refresh token");
                }

                val member = optionalMember.get();

                val id = UUID.randomUUID().toString();
                val scopes = new ArrayList<String>();

                if (member.getIsAdmin()) {
                    scopes.add("ADMIN");
                }

                val newAccessToken = jwtService.createNewJWT(id, member.getId().toString(), member.getEmail(), scopes);
                val newRefreshToken = jwtService.createNewJWTRefresh(id, member.getId().toString());

                return new TokenResponse(newAccessToken, newRefreshToken, "Bearer", LocalDateTime.now().plusDays(14).toEpochSecond(ZoneOffset.UTC), LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC));
            }
            default -> throw new IllegalArgumentException("Not supported grant type: " + grantType);
        }
    }

    @Operation(
            summary = "Register user",
            operationId = "register",
            tags = {"Authorization"}
    )
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<TokenResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Member", required = true)
            @RequestBody(required = true)
            RegisterDTO registerdto
    ) throws GeneralSecurityException, IOException {
        String passwordHash = BCrypt.hashpw(registerdto.getPassword(), BCrypt.gensalt());
        try{
            memberService.create(new MemberEntity(UUID.randomUUID(), registerdto.getEmail(), registerdto.getFirstname(), registerdto.getLastname(), passwordHash, false));
        } catch(UserAlreadyExistsException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(getToken("password", "", registerdto.getEmail(), registerdto.getPassword()), HttpStatus.OK);
    }
}
