package me.heartmon.global.req;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.heartmon.domain.member.entity.Member;
import me.heartmon.domain.member.service.MemberService;
import me.heartmon.global.resultData.ResultData;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

@RequestScope
@Component
public class Req {
    private final MemberService memberService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final User user;
    private Member member = null;

    public Req(MemberService memberService, HttpServletRequest request, HttpServletResponse response) {
        this.memberService = memberService;
        this.request = request;
        this.response = response;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            this.user = (User) authentication.getPrincipal();
        } else {
            this.user = null;
        }
    }

    public boolean isSignin() {
        return user != null;
    }

    public boolean isLogout() {
        return !isSignin();
    }

    public Member getMember() {
        if (isLogout()) return null;

        return member != null ? member
                : memberService.findByUsername(user.getUsername()).orElseThrow();
    }

    public String historyBack(String msg) {
        String referer = request.getHeader("referer");
        String key = "historyBackErrorMsg___" + referer;
        request.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        request.setAttribute("historyBackErrorMsg", msg);
        response.setStatus(SC_BAD_REQUEST);
        return "common/msg";
    }

    public String historyBack(ResultData data) {
        return historyBack(data.getMessage());
    }

    public String redirectWithMsg(String url, ResultData data) {
        return "redirect:" + urlWithMsg(url, data.getMessage());
    }

    private String urlWithMsg(String url, String msg) {
        return Ut.url.modifyQueryParam(url, "msg", Ut.url.encode(msg));
    }
}
