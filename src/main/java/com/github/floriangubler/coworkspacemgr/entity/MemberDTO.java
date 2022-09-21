package com.github.floriangubler.coworkspacemgr.entity;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    @NotNull
    String email;

    @NotNull
    String firstname;

    @NotNull
    String lastname;

    @NotNull
    String password;

    Boolean isAdmin;
}
