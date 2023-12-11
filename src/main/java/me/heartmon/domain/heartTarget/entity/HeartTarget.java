package me.heartmon.domain.heartTarget.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.heartmon.domain.instaMember.entity.InstaMember;
import me.heartmon.global.entity.BaseEntity;

import static jakarta.persistence.FetchType.*;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HeartTarget extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private InstaMember fromInstaMember;
    private String fromInstaMemberUsername;

    @ManyToOne(fetch = LAZY)
    private InstaMember toInstaMember;
    private String toInstaMemberUsername;

    private int reasonPoint;

    public String getWordOfReason() {
        return switch (this.reasonPoint) {
            case 1 -> "외모";
            case 2 -> "성격";
            case 3 -> "취향";
            default -> "기타";
        };
    }
}
