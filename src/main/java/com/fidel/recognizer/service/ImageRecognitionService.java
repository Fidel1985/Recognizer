package com.fidel.recognizer.service;

import com.google.api.services.vision.v1.model.FaceAnnotation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ImageRecognitionService {
    int MAX_RESULTS = 20;
    List<FaceAnnotation> detectFaces(Path path, int maxResults) throws IOException;
    void writeWithFaces(Path inputPath, Path outputPath, List<FaceAnnotation> faces) throws IOException;
}
