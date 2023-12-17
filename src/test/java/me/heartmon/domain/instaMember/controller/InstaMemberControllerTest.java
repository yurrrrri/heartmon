package me.heartmon.domain.instaMember.controller;

import me.heartmon.domain.instaMember.service.InstaMemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
class InstaMemberControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private InstaMemberService instaMemberService;

    @DisplayName("/insta-member/connect 엔드포인트는 insta-memer/connect 뷰를 보여준다.")
    @WithMockUser
    @Test
    void connectForm() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(get("/insta-member/connect"));

        // then
        resultActions
                .andExpect(handler().handlerType(InstaMemberController.class))
                .andExpect(handler().methodName("connectForm"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("insta-member/connect"));
    }

    @DisplayName("유효한 인스타그램 아이디를 입력하면 등록에 성공하고, /usr/me 로 리디렉션 된다.")
    @WithUserDetails("userForTest")
    @Test
    void givenValidInstaName_whenConnecting_thenSuccessAndRedirect() throws Exception {
        // given
        String validName = "testInstaName";

        // when
        ResultActions resultActions = mvc
                .perform(post("/insta-member/connect")
                        .with(csrf())
                        .param("username", validName)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/usr/me?**"));
        assertThat(instaMemberService.findByUsername(validName)).isPresent();
    }

    @DisplayName("유효하지 않은 인스타그램 아이디로 등록 시도하면 Client Error.")
    @WithUserDetails("userForTest")
    @Test
    void givenInValidInstaName_whenConnecting_thenFail() throws Exception {
        // given
        String invalidName = "tst";

        // when
        ResultActions resultActions = mvc
                .perform(post("/insta-member/connect")
                        .with(csrf())
                        .param("username", invalidName)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then
        resultActions.andExpect(status().is4xxClientError());
    }
}