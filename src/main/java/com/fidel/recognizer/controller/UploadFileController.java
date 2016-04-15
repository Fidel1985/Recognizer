package com.fidel.recognizer.controller;

import com.fidel.recognizer.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;

import java.net.BindException;

import com.fidel.recognizer.entity.UploadItem;

@Controller
@RequestMapping(value = "/uploadFile")
public class UploadFileController {

    @Autowired
    private UploadFileService uploadFileService;

    @RequestMapping(method = RequestMethod.GET)
    public String getUploadForm(Model model) {
        model.addAttribute(new UploadItem());
        return "uploadFile";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String uploadFileHandler(UploadItem uploadItem, HttpSession session) {
        try {
            session.setAttribute("uploadFile", uploadFileService.uploadFile(uploadItem));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return "redirect:/uploadFileIndex";
    }
}