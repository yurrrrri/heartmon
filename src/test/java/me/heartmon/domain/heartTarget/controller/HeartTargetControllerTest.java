package me.heartmon.domain.heartTarget.controller;

import me.heartmon.domain.heartTarget.dto.HeartTargetDto;
import me.heartmon.domain.heartTarget.service.HeartTargetService;
import me.heartmon.domain.instaMember.dto.InstaMemberConnectDto;
import me.heartmon.domain.instaMember.service.InstaMemberService;
import me.heartmon.domain.member.entity.Member;
import me.heartmon.domain.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class HeartTargetControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private InstaMemberService instaMemberService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private HeartTargetService heartTargetService;

    @DisplayName("/heart-target/heart 엔드포인트는 heart-target/heart 뷰를 보여준다.")
    @WithUserDetails("userForTest")
    @Test
    void heartForm() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(get("/heart-target/heart"));

        // then
        resultActions
                .andExpect(handler().handlerType(HeartTargetController.class))
                .andExpect(handler().methodName("heartForm"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("heart-target/heart"));
    }

    @DisplayName("인스타그램 아이디가 등록되지 않은 유저가 하트를 시도하면 실패하고, /insta-member/connect 로 리디렉션 된다.")
    @WithUserDetails("userForTest")
    @Test
    void givenUnconnectedUser_whenHeart_thenFailAndRedirectToConnectPage() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(post("/heart-target/heart")
                        .with(csrf())
                        .param("username", "otherUser")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then
        resultActions
                .andExpect(handler().handlerType(HeartTargetController.class))
                .andExpect(handler().methodName("heart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/insta-member/connect"));
    }

    @DisplayName("인스타그램 아이디가 등록된 유저가 본인에게 하트를 시도하면 Client Error.")
    @WithUserDetails("userForTest")
    @Test
    void givenConnectedUser_whenHeartOneSelf_thenFail() throws Exception {
        // given
        instaMemberService.connect(
                memberService.findByUsername("userForTest").get(),
                new InstaMemberConnectDto("testInsta")
        );

        // when
        ResultActions resultActions = mvc
                .perform(post("/heart-target/heart")
                        .with(csrf())
                        .param("username", "testInsta")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then
        resultActions
                .andExpect(handler().handlerType(HeartTargetController.class))
                .andExpect(handler().methodName("heart"))
                .andExpect(status().is4xxClientError());
    }

    @DisplayName("인스타그램 아이디가 등록된 유저가 하트를 시도하면 성공하고, /heart-target/list 로 리디렉션 된다.")
    @WithUserDetails("userForTest")
    @Test
    void givenConnectedUser_whenHeart_thenSuccessAndRedirectToHeartList() throws Exception {
        // given
        instaMemberService.connect(
                memberService.findByUsername("userForTest").get(),
                new InstaMemberConnectDto("testInsta")
        );

        // when
        ResultActions resultActions = mvc
                .perform(post("/heart-target/heart")
                        .with(csrf())
                        .param("username", "otherUser")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then
        resultActions
                .andExpect(handler().handlerType(HeartTargetController.class))
                .andExpect(handler().methodName("heart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/heart-target/list?**"));
    }

    @DisplayName("/heart-target/list 엔드포인트는 heart-target/list 뷰를 보여준다.")
    @WithUserDetails("userForTest")
    @Test
    void listForm() throws Exception {
        // given
        instaMemberService.connect(
                memberService.findByUsername("userForTest").get(),
                new InstaMemberConnectDto("testInsta")
        );

        // when
        ResultActions resultActions = mvc
                .perform(get("/heart-target/list"));

        // then
        resultActions
                .andExpect(handler().handlerType(HeartTargetController.class))
                .andExpect(handler().methodName("list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("heart-target/list"));
    }

    @DisplayName("인스타그램 아이디가 등록되지 않은 유저가 list 페이지를 조회할 때 /insta-member/connect 로 리디렉션 된다.")
    @WithUserDetails("userForTest")
    @Test
    void givenUnconnectedUser_whenViewingList_thenRedirectToConnectPage() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(get("/heart-target/list"));

        // then
        resultActions
                .andExpect(handler().handlerType(HeartTargetController.class))
                .andExpect(handler().methodName("list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/insta-member/connect"));
    }

    @DisplayName("유효한 유저가 하트를 취소하면 성공하고, /heart-target/list 로 리디렉션 된다.")
    @WithUserDetails("userForTest")
    @Test
    void whenCancel_thenSuccessAndRedirectToListPage() throws Exception {
        // given
        Member member = memberService.findByUsername("userForTest").get();
        instaMemberService.connect(member, new InstaMemberConnectDto("testInsta"));
        heartTargetService.heart(member, new HeartTargetDto("otherUser", 1));

        // when
        ResultActions resultActions = mvc
                .perform(get("/heart-target/1"));

        // then
        resultActions
                .andExpect(handler().handlerType(HeartTargetController.class))
                .andExpect(handler().methodName("cancel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/heart-target/list?**"));
    }
}