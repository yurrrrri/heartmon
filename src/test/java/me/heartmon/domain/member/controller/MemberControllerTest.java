package me.heartmon.domain.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.heartmon.domain.member.dto.MemberSignupDto;
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
    private ObjectMapper mapper;

    @Test
    void signin() throws Exception {
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
    void signup() throws Exception {
        // given
        MemberSignupDto dto = new MemberSignupDto("user1", "test1234");

        //when
        ResultActions resultActions = mvc
                .perform(post("/usr/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)));

        // then
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}