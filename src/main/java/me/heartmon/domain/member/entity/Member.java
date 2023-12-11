package me.heartmon.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.heartmon.domain.instaMember.entity.InstaMember;
import me.heartmon.global.entity.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Column(unique = true)
    private String username;
    private String password;

    @OneToOne
    private InstaMember instaMember;

    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        if (isAdmin()) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        return grantedAuthorities;
    }

    public boolean isAdmin() {
        return this.username.equals("admin");
    }

    public boolean isConnectedInstaMember() {
        return instaMember != null;
    }

    public void connectInstaMember(InstaMember instaMember) {
        this.instaMember = instaMember;
    }
}
