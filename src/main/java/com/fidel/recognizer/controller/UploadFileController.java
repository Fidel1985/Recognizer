package com.fidel.recognizer.controller;

import com.fidel.recognizer.service.ImageRecognitionService;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

import com.fidel.recognizer.entity.UploadItem;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/uploadFile")
public class UploadFileController {

    //@Autowired
    //ImageRecognitionService imageRecognitionService;

    private String uploadFolderPath;

    public String getUploadFolderPath() {
        return uploadFolderPath;
    }

    public void setUploadFolderPath(String uploadFolderPath) {
        this.uploadFolderPath = uploadFolderPath;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getUploadForm(Model model) {
        model.addAttribute(new UploadItem());
        return "/uploadFile";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String uploadFileHandler(UploadItem uploadItem, HttpServletRequest request,
                  HttpServletResponse response, Object command, BindException errors,
                  HttpSession session) {

        try {
            MultipartFile multipartFile = uploadItem.getFileData();

            // Creating the directory to store file
            String rootPath = System.getProperty("catalina.home");
            String outputDir = rootPath + File.separator + "webapps" + File.separator + "ROOT" + File.separator + "resources";
            Path inputDir = Paths.get("/resources/" + multipartFile.getOriginalFilename());
            //String inputDir = "/resources/" + multipartFile.getOriginalFilename();
            File dir = new File(outputDir);
            if (!dir.exists())
                dir.mkdirs(); //???

            InputStream inputStream = null;
            OutputStream outputStream = null;
            if (multipartFile.getSize() > 0) {
                inputStream = multipartFile.getInputStream();

                outputStream = new FileOutputStream(dir.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());

                int readBytes = 0;
                byte[] buffer = new byte[8192];
                while ((readBytes = inputStream.read(buffer, 0, 8192)) != -1) {
                    outputStream.write(buffer, 0, readBytes);
                }
                outputStream.close();
                inputStream.close();

                //session.setAttribute("uploadFile", dir.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());
                session.setAttribute("uploadFile", "/resources/" + multipartFile.getOriginalFilename());
                //imageRecognitionService.recognize(multipartFile, outputDir, inputDir);
            }
        } catch (IOException e) {
        //} catch (IOException|GeneralSecurityException e) {
            e.printStackTrace();
        }
        return "redirect:/uploadFileIndex";
    }
}