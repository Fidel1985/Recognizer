package com.fidel.recognizer.controller;

import com.fidel.recognizer.entity.UploadItem;
import com.fidel.recognizer.service.ImageRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping(value = "/recognizeFaces")
public class RecognizeFileController {

    @Autowired
    ImageRecognitionService imageRecognitionService;

    @RequestMapping(method = RequestMethod.GET)
    public String recognizeFaces(UploadItem uploadItem, HttpServletRequest request,
                                 HttpServletResponse response, Object command, BindException errors,
                                 HttpSession session) {

        String imagePath = (String) session.getAttribute("uploadFile");
        String rootPath = System.getProperty("catalina.home");
        Path path = Paths.get(rootPath + File.separator + "webapps" + File.separator + "ROOT" + File.separator + imagePath);

        try{
            imageRecognitionService.recognize(path, path);
        }catch(IOException | GeneralSecurityException e){
            e.printStackTrace();
        }

        return "redirect:/uploadFileIndex";
    }


}
