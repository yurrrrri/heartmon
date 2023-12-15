package me.heartmon.domain.member.controller;

import me.heartmon.domain.member.dto.MemberSignupDto;
import me.heartmon.domain.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    MemberService memberService;

    @Test
    void signinForm() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(get("/usr/signin"));

        // then
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("signin"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("usr/signin"));
    }

    @Test
    void givenUser_whenSignin_thenRedirectToMypage() throws Exception {
        // given
        memberService.signup(new MemberSignupDto("testUser", "test1234"));

        // when
        ResultActions resultActions = mvc
                .perform(post("/usr/signin")
                        .with(csrf())
                        .param("username", "testUser")
                        .param("password", "test1234"));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usr/me"));
    }

    @Test
    void signupForm() throws Exception {
        // when
        ResultActions resultActions = mvc
                .perform(get("/usr/signup"));

        // then
        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("signupForm"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("usr/signup"));
    }

    @Test
    void whenSignup_thenSuccessAndRedirect() throws Exception {
        // given

        //when
        ResultActions resultActions = mvc
                .perform(post("/usr/signup")
                        .with(csrf())
                        .param("username", "testUser")
                        .param("password", "test1234")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/usr/signin?**"));
    }

    @Test
    void givenInvalidPassword_whenSignup_thenClientError() throws Exception {
        // given
        String invalidPassword = "123";

        //when
        ResultActions resultActions = mvc
                .perform(post("/usr/signup")
                        .with(csrf())
                        .param("username", "testUser")
                        .param("password", invalidPassword)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED));

        // then
        resultActions.andExpect(status().is4xxClientError());
    }
}