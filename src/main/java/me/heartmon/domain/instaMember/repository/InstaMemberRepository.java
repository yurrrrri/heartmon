package me.heartmon.domain.instaMember.repository;

import me.heartmon.domain.instaMember.entity.InstaMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstaMemberRepository extends JpaRepository<InstaMember, Long> {
}
