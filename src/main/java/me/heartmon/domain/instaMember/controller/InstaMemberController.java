package me.heartmon.domain.instaMember.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.heartmon.domain.instaMember.dto.InstaMemberConnectDto;
import me.heartmon.domain.instaMember.service.InstaMemberService;
import me.heartmon.global.req.Req;
import me.heartmon.global.resultData.ResultData;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@Controller
@RequestMapping("/insta-member")
public class InstaMemberController {

    private final InstaMemberService instaMemberService;
    private final Req req;

    @GetMapping("/connect")
    public String connectForm(Model model) {
        model.addAttribute("instaMemberConnectDto", new InstaMemberConnectDto());
        return "insta-member/connect";
    }

    @PostMapping("/connect")
    public String connect(@Valid InstaMemberConnectDto dto) {
        ResultData<String> result = instaMemberService.connect(req.getMember(), dto);

        if (result.isFail()) return req.historyBack(result);

        return req.redirectWithMsg("/usr/me", result);
    }
}
