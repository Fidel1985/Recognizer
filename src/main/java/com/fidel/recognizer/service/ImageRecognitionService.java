package com.fidel.recognizer.service;

import com.fidel.recognizer.entity.VisionInstance;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class ImageRecognitionService {
    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "visionproject-1277";

    //private static final int MAX_RESULTS = 4;
    private static final int MAX_RESULTS = 20;


    public void recognize(Path outputPath, Path inputPath)
            throws IOException, GeneralSecurityException {

        VisionInstance app = new VisionInstance(getVisionService());
        List<FaceAnnotation> faces = app.detectFaces(inputPath, MAX_RESULTS);
        System.out.printf("Found %d face%s\n", faces.size(), faces.size() == 1 ? "" : "s");
        System.out.printf("Writing to file %s\n", outputPath);
        app.writeWithFaces(inputPath, outputPath, faces);

    }

    public static Vision getVisionService() throws IOException, GeneralSecurityException {
        GoogleCredential credential =
                GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

}
