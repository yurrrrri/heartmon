package me.heartmon.domain.heartTarget.repository;

import me.heartmon.domain.heartTarget.entity.HeartTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartTargetRepository extends JpaRepository<HeartTarget, Long> {
}
