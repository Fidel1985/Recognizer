package com.fidel.recognizer.service;

import com.fidel.recognizer.entity.VisionLabelInstance;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class LabelRecognitionService {
    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "VisionLabel/1.0";

    public static final int MAX_LABELS = 3;

    /**
     * Annotates an image using the Vision API.
     */
    public List<EntityAnnotation> recognizeLabels(Path imagePath) throws IOException, GeneralSecurityException {


        VisionLabelInstance visionLabelInstance = new VisionLabelInstance(getVisionService());
        //printLabels(System.out, imagePath, visionLabelInstance.labelImage(imagePath, MAX_LABELS));
        return visionLabelInstance.labelImage(imagePath, MAX_LABELS);
    }


    // [START authenticate]
    /**
     * Connects to the Vision API using Application Default Credentials.
     */
    public Vision getVisionService() throws IOException, GeneralSecurityException {
        GoogleCredential credential =
                GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    // [END authenticate]

    /**
     * Prints the labels received from the Vision API.
     */
    public static void printLabels(PrintStream out, Path imagePath, List<EntityAnnotation> labels) {
        out.printf("Labels for image %s:\n", imagePath);
        for (EntityAnnotation label : labels) {
            out.printf(
                    "\t%s (score: %.3f)\n",
                    label.getDescription(),
                    label.getScore());
        }
        if (labels.isEmpty()) {
            out.println("\tNo labels found.");
        }
    }
}
