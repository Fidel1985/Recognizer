package com.fidel.recognizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/uploadFileIndex")
public class UploadFileIndexController {

    // Display the form on the get request
    @RequestMapping(method = RequestMethod.GET)
    public String showUploadedFile(Model model) {
        return "uploadFileIndex";
    }

}
