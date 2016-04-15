package com.fidel.recognizer.controller;

import com.fidel.recognizer.service.ImageRecognitionService;
import com.fidel.recognizer.service.LabelRecognitionService;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.FaceAnnotation;
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
import java.util.List;

@RequestMapping(value = "/recognize")
@Controller
public class RecognizeFileController {

    @Autowired
    ImageRecognitionService imageRecognitionService;

    @Autowired
    LabelRecognitionService labelRecognitionService;

    @RequestMapping(params = "recognizeFaces", method = RequestMethod.GET)
    public String recognizeFaces(HttpSession session) {

        String imagePath = (String) session.getAttribute("uploadFile");
        String rootPath = System.getProperty("catalina.home");
        Path path = Paths.get(rootPath + File.separator + "webapps" + File.separator + "ROOT" + File.separator + imagePath);

        try{
            List<FaceAnnotation> faces = imageRecognitionService.detectFaces(path, ImageRecognitionService.MAX_RESULTS);
            imageRecognitionService.writeWithFaces(path, path, faces);
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadFileIndex";
    }

    @RequestMapping(params = "recognizeLabels", method = RequestMethod.GET)
    public String recognizeLabels(HttpSession session) {

        String imagePath = (String) session.getAttribute("uploadFile");
        String rootPath = System.getProperty("catalina.home");
        Path path = Paths.get(rootPath + File.separator + "webapps" + File.separator + "ROOT" + File.separator + imagePath);

        try {
            List<EntityAnnotation> labels = labelRecognitionService.labelImage(path, labelRecognitionService.MAX_LABELS);
            session.setAttribute("labels", labels);
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadFileIndex";
    }

}
