package com.fidel.recognizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String home() {
        return "redirect:/uploadFile";
    }

    @RequestMapping("/denied")
    public String accessDenied() {
        return "accessDenied";
    }

}
