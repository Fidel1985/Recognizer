package com.fidel.recognizer.service;

import com.fidel.recognizer.entity.UploadItem;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class UploadFileServiceImpl implements UploadFileService {

    @Override
    public String uploadFile(UploadItem uploadItem) throws IOException{

        MultipartFile multipartFile = uploadItem.getFileData();

        // Creating the directory to store file
        String rootPath = System.getProperty("catalina.home");
        String outputDir = rootPath + File.separator + "webapps" + File.separator + "ROOT" + File.separator +
                "resources" + File.separator;

        File dir = new File(outputDir);
        if (!dir.exists())
            dir.mkdirs();

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
        }
        return ("/resources/" + multipartFile.getOriginalFilename());
    }
}
