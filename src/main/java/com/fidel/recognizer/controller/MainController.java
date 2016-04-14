package com.fidel.recognizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/")
    public String home() {
        System.out.println("I'm in the main controller");
        return "redirect:/uploadFile";
    }

}
