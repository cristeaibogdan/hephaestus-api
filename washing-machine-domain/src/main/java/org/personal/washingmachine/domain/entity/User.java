package org.personal.washingmachine.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@Entity
@Table(schema = "public", name = "user")
public class User extends BaseEntity {

    @Column(name = "code")
    private String code;

    @Column(name = "organization")
    private String organization;

    @Column(name = "country")
    private String country;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Setter(NONE)
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public User(String code, String organization, String country, String email, String username, String password) {
        this.code = code;
        this.organization = organization;
        this.country = country;
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
