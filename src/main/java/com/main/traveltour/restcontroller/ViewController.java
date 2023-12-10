package com.main.traveltour.restcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

    @GetMapping({"/sign-in", "/", ""})
    public String login(@RequestParam(name = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "email/sign-in";
    }

    @GetMapping("/manager/dashboard")
    public String dashboard() {
        return "index";
    }

    @GetMapping("/manager/chat")
    public String chat() {
        return "index";
    }

    @GetMapping("/manager/tour")
    public String tour() {
        return "index";
    }
}
