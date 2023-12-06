package me.heartmon.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/usr")
public class MemberController {

    @GetMapping("/signup")
    public String signup() {
        return "usr/signup";
    }
}
