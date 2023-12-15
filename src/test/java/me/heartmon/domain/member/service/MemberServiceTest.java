package me.heartmon.domain.member.service;

import me.heartmon.domain.member.dto.MemberSignupDto;
import me.heartmon.domain.member.entity.Member;
import me.heartmon.domain.member.repository.MemberRepository;
import me.heartmon.global.resultData.ResultData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("유효한 유저 정보를 입력하면 회원가입에 성공한다.")
    @Test
    void givenValidUserInfo_whenSignup_thenSuccess() {
        // given
        MemberSignupDto dto = new MemberSignupDto("testUser", "test1234");
        given(memberService.findByUsername("testUser")).willReturn(Optional.empty());

        // when
        ResultData result = memberService.signup(dto);

        // then
        assertThat(result.getCode()).startsWith("S");
        then(memberRepository).should().save(any(Member.class));
    }

    @DisplayName("이미 등록되어 있는 유저 아이디를 입력하면 회원가입에 실패한다.")
    @Test
    void givenAlreadySavedUserInfo_whenSignup_thenFail() {
        // given
        Member member = Member.builder()
                .username("testUser")
                .password(passwordEncoder.encode("test1234"))
                .build();
        MemberSignupDto dto = new MemberSignupDto("testUser", "1234");
        given(memberService.findByUsername("testUser")).willReturn(Optional.of(member));

        // when
        ResultData result = memberService.signup(dto);

        // then
        assertThat(result.getCode()).startsWith("F");
        then(memberRepository).should().findByUsername("testUser");
        then(memberRepository).shouldHaveNoMoreInteractions();
    }
}