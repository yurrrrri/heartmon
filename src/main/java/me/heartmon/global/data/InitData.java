package me.heartmon.global.data;

import lombok.RequiredArgsConstructor;
import me.heartmon.domain.member.dto.MemberSignupDto;
import me.heartmon.domain.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InitData implements CommandLineRunner {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        memberService.signup(new MemberSignupDto("userForTest", passwordEncoder.encode("test1234")));
    }
}