package me.heartmon.global.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"", "/"})
    public String home() {
        return "redirect:/usr/me";
    }

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/adm/main")
    public String admMain() {
        return "adm/main";
    }
}
