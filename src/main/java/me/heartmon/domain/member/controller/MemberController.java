package me.heartmon.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.heartmon.domain.member.dto.MemberSignupDto;
import me.heartmon.domain.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/usr")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signin")
    public String signin() {
        return "usr/signin";
    }

    @GetMapping("/signup")
    public String signup() {
        return "usr/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberSignupDto dto) {
        memberService.signup(dto);
        return "redirect:/";
    }
}
