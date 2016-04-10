package com.fidel.recognizer.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.*;
import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class ImageRecognitionService {
    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    //private static final String APPLICATION_NAME = "Google-VisionFaceDetectSample/1.0";
    private static final String APPLICATION_NAME = "visionproject-1277";

    private static final int MAX_RESULTS = 4;



    public void recognize(MultipartFile multipartFile, String outputPath, java.nio.file.Path inputPath) throws IOException, GeneralSecurityException {

        ImageRecognitionService app = new ImageRecognitionService(getVisionService());
        List<FaceAnnotation> faces = app.detectFaces(inputPath, MAX_RESULTS);
        System.out.printf("Found %d face%s\n", faces.size(), faces.size() == 1 ? "" : "s");
        System.out.printf("Writing to file %s\n", outputPath);


    }

    public static Vision getVisionService() throws IOException, GeneralSecurityException {
        GoogleCredential credential =
                GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private final Vision vision;

    /**
     * Constructs a {@link ImageRecognitionService} which connects to the Vision API.
     */
    public ImageRecognitionService (Vision vision) {
        this.vision = vision;
    }

    // [START detect_face]
    /**
     * Gets up to {@code maxResults} faces for an image stored at {@code path}.
     */
    public List<FaceAnnotation> detectFaces(Path path, int maxResults) throws IOException {
        byte[] data = Files.readAllBytes(path);

        AnnotateImageRequest request =
                new AnnotateImageRequest()
                        .setImage(new Image().encodeContent(data))
                        .setFeatures(ImmutableList.of(
                                new Feature()
                                        .setType("FACE_DETECTION")
                                        .setMaxResults(maxResults)));
        Vision.Images.Annotate annotate =
                vision.images()
                        .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotate.setDisableGZipContent(true);

        BatchAnnotateImagesResponse batchResponse = annotate.execute();
        assert batchResponse.getResponses().size() == 1;
        AnnotateImageResponse response = batchResponse.getResponses().get(0);
        if (response.getFaceAnnotations() == null) {
            throw new IOException(
                    response.getError() != null
                            ? response.getError().getMessage()
                            : "Unknown error getting image annotations");
        }
        return response.getFaceAnnotations();
    }
    // [END detect_face]

}
