package com.github.floriangubler.coworkspacemgr.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "MEMBER")
public class MemberEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    UUID id = UUID.randomUUID();

    @Column(name = "email", nullable = false)
    @NotNull
    String email;

    @Column(name = "firstname", nullable = false)
    @NotNull
    String firstname;

    @Column(name = "lastname", nullable = false)
    @NotNull
    String lastname;

    @Column(name = "password", nullable = false)
    @NotNull
    String password;

    @Column(name = "is_admin", nullable = false)
    Boolean isAdmin = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MemberEntity that = (MemberEntity) o;
        return id != null && Objects.equals(id, that.id) &&
                email != null && Objects.equals(email, that.email) &&
                password != null && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
