package me.heartmon.domain.member.service;

import lombok.RequiredArgsConstructor;
import me.heartmon.domain.member.entity.Member;
import me.heartmon.domain.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow();
    }

    public Member signup(String username, String password) {
        return memberRepository.save(Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build());
    }
}
