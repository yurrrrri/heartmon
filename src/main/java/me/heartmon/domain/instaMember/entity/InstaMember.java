package me.heartmon.domain.instaMember.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.heartmon.domain.heartTarget.entity.HeartTarget;
import me.heartmon.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InstaMember extends BaseEntity {

    @Column(unique = true)
    private String username;
    private boolean isSelf;

    @OneToMany(mappedBy = "fromInstaMember", cascade = CascadeType.ALL)
    @OrderBy("id desc")
    @Builder.Default
    private List<HeartTarget> myHeartTargets = new ArrayList<>();

    @OneToMany(mappedBy = "toInstaMember", cascade = CascadeType.ALL)
    @OrderBy("id desc")
    @Builder.Default
    private List<HeartTarget> yourHeartTargets = new ArrayList<>();

    public void addMyHeartTarget(HeartTarget heartTarget) {
        myHeartTargets.add(0, heartTarget);
    }

    public void addYourHeartTarget(HeartTarget heartTarget) {
        yourHeartTargets.add(0, heartTarget);
    }

    public void setIsSelf() {
        this.isSelf = true;
    }
}
