package me.heartmon.domain.instaMember.service;

import me.heartmon.domain.instaMember.dto.InstaMemberConnectDto;
import me.heartmon.domain.instaMember.entity.InstaMember;
import me.heartmon.domain.instaMember.repository.InstaMemberRepository;
import me.heartmon.domain.member.entity.Member;
import me.heartmon.global.resultData.ResultData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class InstaMemberServiceTest {

    @InjectMocks
    private InstaMemberService instaMemberService;
    @Mock
    private InstaMemberRepository instaMemberRepository;

    Member member = Member.builder()
            .id(1L)
            .username("testUser")
            .build();

    @DisplayName("인스타그램 아이디를 등록하면 성공한다.")
    @Test
    void whenConnect_thenSuccess() {
        // given

        // when
        ResultData result = instaMemberService.connect(member, new InstaMemberConnectDto("testInstaName"));

        // then
        assertThat(result.getCode()).startsWith("S");
        then(instaMemberRepository).should().save(any(InstaMember.class));
    }

    @DisplayName("다른 사람에 의해 이미 저장된 인스타그램 아이디를 등록하면 성공하고, isSelf()는 true로 변경된다.")
    @Test
    void givenAlreadySavedInstaNameButNotSelf_whenConnect_thenSuccess() {
        // given
        InstaMember instaMember = createInstaNember("testInstaName", false);
        given(instaMemberRepository.findByUsername("testInstaName")).willReturn(Optional.ofNullable(instaMember));

        // when
        ResultData<String> result = instaMemberService.connect(member, new InstaMemberConnectDto("testInstaName"));

        // then
        assertThat(result.getCode()).startsWith("S");
        then(instaMemberRepository).should().findByUsername("testInstaName");

        assertThat(instaMemberService.findByUsername("testInstaName").get().isSelf()).isTrue();
    }

    @DisplayName("이미 등록되어 있는 인스타그램 아이디를 재등록하면 실패한다.")
    @Test
    void givenAlreadySelfSavedInstaName_whenConnect_thenFail() {
        // given
        InstaMember instaMember = createInstaNember("testInstaName", true);
        given(instaMemberRepository.findByUsername("testInstaName")).willReturn(Optional.ofNullable(instaMember));

        // when
        ResultData<String> result = instaMemberService.connect(member, new InstaMemberConnectDto("testInstaName"));

        // then
        assertThat(result.getCode()).startsWith("F");
        then(instaMemberRepository).should().findByUsername("testInstaName");
    }

    @DisplayName("등록되어 있는 인스타그램 아이디를 findByUsernameOrCreate 하면 조회에 성공한다.")
    @Test
    void givenInstaMember_whenFindByUsernameOrCreate_thenFound() {
        // given
        InstaMember instaMember = createInstaNember("testInstaName", true);
        given(instaMemberRepository.findByUsername("testInstaName")).willReturn(Optional.ofNullable(instaMember));

        // when
        ResultData<InstaMember> result = instaMemberService.findByUsernameOrCreate("testInstaName");

        // then
        assertThat(result.getMessage()).contains("조회");
        assertThat(result.getData()).isSameAs(instaMember);
    }

    @DisplayName("등록되어 있지 않은 인스타그램 아이디를 findByUsernameOrCreate 하면 생성 및 저장에 성공한다.")
    @Test
    void givenNothing_whenFindByUsernameOrCreate_thenCreate() {
        // given
        given(instaMemberRepository.findByUsername("testInstaName")).willReturn(Optional.empty());

        // when
        ResultData<InstaMember> result = instaMemberService.findByUsernameOrCreate("testInstaName");

        // then
        assertThat(result.getMessage()).contains("등록");
        then(instaMemberRepository).should().findByUsername("testInstaName");
        then(instaMemberRepository).should().save(any(InstaMember.class));
    }

    private static InstaMember createInstaNember(String username, boolean isSelf) {
        return InstaMember.builder()
                .username(username)
                .isSelf(isSelf)
                .build();
    }
}
