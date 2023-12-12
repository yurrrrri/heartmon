package me.heartmon.domain.member.service;

import lombok.RequiredArgsConstructor;
import me.heartmon.domain.member.dto.MemberSignupDto;
import me.heartmon.domain.member.entity.Member;
import me.heartmon.domain.member.repository.MemberRepository;
import me.heartmon.global.resultData.ResultData;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public ResultData signup(MemberSignupDto dto) {
        if (findByUsername(dto.getUsername()).isPresent()) {
            return ResultData.of("F-M1", "이미 등록된 아이디입니다.");
        }

        memberRepository.save(Member.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build());
        return ResultData.of("S-M1", "회원가입이 완료되었습니다.");
    }
}
