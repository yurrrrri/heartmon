package me.heartmon.domain.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.heartmon.domain.member.dto.MemberSignupDto;
import me.heartmon.domain.member.service.MemberService;
import me.heartmon.global.req.Req;
import me.heartmon.global.resultData.ResultData;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@PreAuthorize("isAnonymous()")
@RequestMapping("/usr")
public class MemberController {

    private final MemberService memberService;
    private final Req req;

    @GetMapping("/signin")
    public String signin() {
        return "usr/signin";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "usr/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody MemberSignupDto dto) {
        ResultData result = memberService.signup(dto);

        if (result.isFail()) return req.historyBack(result.getMessage());

        return req.redirectWithMsg("/usr/signin", result);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String showMe(Model model) {
        model.addAttribute("member", req.getMember());
        return "usr/me";
    }
}
