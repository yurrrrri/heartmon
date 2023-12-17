package me.heartmon.domain.heartTarget.service;

import me.heartmon.domain.heartTarget.dto.HeartTargetDto;
import me.heartmon.domain.heartTarget.entity.HeartTarget;
import me.heartmon.domain.heartTarget.repository.HeartTargetRepository;
import me.heartmon.domain.instaMember.entity.InstaMember;
import me.heartmon.domain.instaMember.service.InstaMemberService;
import me.heartmon.domain.member.entity.Member;
import me.heartmon.global.resultData.ResultData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class HeartTargetServiceTest {

    @InjectMocks
    private HeartTargetService heartTargetService;
    @Mock
    private HeartTargetRepository heartTargetRepository;
    @Mock
    private InstaMemberService instaMemberService;

    @DisplayName("인스타그램 아이디가 등록되어 있지 않은 유저가 하트를 시도하면 실패한다.")
    @Test
    void givenUnconnectedUser_whenHeart_thenFail() {
        // given
        Member member = Member.builder()
                .username("testUser")
                .build();

        // when
        ResultData result = heartTargetService.heart(member, new HeartTargetDto("testInsta", 1));

        // then
        assertThat(result.getCode()).startsWith("F");
        then(heartTargetRepository).shouldHaveNoInteractions();
    }

    @DisplayName("인스타그램 아이디가 등록되어 있는 유저가 본인의 인스타그램 아이디로 하트를 시도하면 실패한다.")
    @Test
    void givenConnectedUser_whenHeartToOneSelf_thenFail() {
        // given
        Member member = Member.builder()
                .username("testUser")
                .instaMember(new InstaMember("testInstaName", true, null, null))
                .build();

        // when
        ResultData result = heartTargetService.heart(member, new HeartTargetDto("testInstaName", 1));

        // then
        assertThat(result.getCode()).startsWith("F");
        then(heartTargetRepository).shouldHaveNoInteractions();
    }

    @DisplayName("인스타그램 아이디가 등록되어 있는 유저가 하트를 시도하면 성공한다.")
    @Test
    void givenConnectedUser_whenHeart_thenSuccess() {
        // given
        Member member = Member.builder()
                .username("testUser")
                .instaMember(new InstaMember("testInstaName", true, new ArrayList<>(), new ArrayList<>()))
                .build();
        InstaMember instaMember = InstaMember.builder()
                .username("testInsta")
                .isSelf(false)
                .build();
        given(instaMemberService.findByUsernameOrCreate("testInsta")).willReturn(
                ResultData.of("S-I3", "인스타그램 아이디를 조회했습니다.", instaMember));

        // when
        ResultData result = heartTargetService.heart(member, new HeartTargetDto("testInsta", 1));

        // then
        assertThat(result.getCode()).startsWith("S");
        then(heartTargetRepository).should().save(any(HeartTarget.class));
        then(heartTargetRepository).shouldHaveNoMoreInteractions();
    }
}