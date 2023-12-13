package me.heartmon.domain.heartTarget.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.heartmon.domain.heartTarget.dto.HeartTargetDto;
import me.heartmon.domain.heartTarget.service.HeartTargetService;
import me.heartmon.domain.instaMember.entity.InstaMember;
import me.heartmon.global.req.Req;
import me.heartmon.global.resultData.ResultData;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@Controller
@RequestMapping("/heart-target")
public class HeartTargetController {

    private final HeartTargetService heartTargetService;
    private final Req req;

    @GetMapping("/heart")
    public String heartForm(Model model) {
        model.addAttribute("heartTargetDto", new HeartTargetDto());
        return "heart-target/heart";
    }

    @PostMapping("/heart")
    public String heart(@Valid HeartTargetDto dto) {
        ResultData result = heartTargetService.heart(req.getMember(), dto);

        if (result.isFail()) return req.historyBack(result);

        return req.redirectWithMsg("/heart-target/list", result);
    }

    @GetMapping("/list")
    public String list(Model model) {
        InstaMember instaMember = req.getMember().getInstaMember();

        if (instaMember == null) {
            return "redirect:/insta-member/connect";
        }

        model.addAttribute("heartTargets", instaMember.getMyHeartTargets());
        return "heart-target/list";
    }

    @GetMapping("/{id}")
    public String cancel(@PathVariable(name = "id") Long id) {
        ResultData result = heartTargetService.cancel(req.getMember(), id);

        if (result.isFail()) return req.historyBack(result);

        return req.redirectWithMsg("/heart-target/list", result);
    }
}
