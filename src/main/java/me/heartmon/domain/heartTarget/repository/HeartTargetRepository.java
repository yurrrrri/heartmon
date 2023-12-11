package me.heartmon.domain.heartTarget.repository;

import me.heartmon.domain.heartTarget.entity.HeartTarget;
import me.heartmon.domain.instaMember.entity.InstaMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeartTargetRepository extends JpaRepository<HeartTarget, Long> {

    List<HeartTarget> findByFromInstaMember(InstaMember instaMember);
    List<HeartTarget> findByFromInstaMember_Id(Long fromInstaMemberId);
}
