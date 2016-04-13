package com.fidel.recognizer.service;

import com.google.api.services.vision.v1.model.EntityAnnotation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface LabelRecognitionService {
    int MAX_LABELS = 3;
    List<EntityAnnotation> labelImage(Path path, int maxResults) throws IOException;
}
