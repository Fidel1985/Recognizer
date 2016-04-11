package com.fidel.recognizer.entity;

import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.*;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VisionLabelInstance {
    private final Vision vision;

    /**
     * Constructs a {@link VisionLabelInstance} which connects to the Vision API.
     */
    public VisionLabelInstance(Vision vision) {
        this.vision = vision;
    }

    /**
     * Gets up to {@code maxResults} labels for an image stored at {@code path}.
     */
    public List<EntityAnnotation> labelImage(Path path, int maxResults) throws IOException {
        // [START construct_request]
        byte[] data = Files.readAllBytes(path);

        AnnotateImageRequest request =
                new AnnotateImageRequest()
                        .setImage(new Image().encodeContent(data))
                        .setFeatures(ImmutableList.of(
                                new Feature()
                                        .setType("LABEL_DETECTION")
                                        .setMaxResults(maxResults)));
        Vision.Images.Annotate annotate =
                vision.images()
                        .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotate.setDisableGZipContent(true);
        // [END construct_request]

        // [START parse_response]
        BatchAnnotateImagesResponse batchResponse = annotate.execute();
        assert batchResponse.getResponses().size() == 1;
        AnnotateImageResponse response = batchResponse.getResponses().get(0);
        if (response.getLabelAnnotations() == null) {
            throw new IOException(
                    response.getError() != null
                            ? response.getError().getMessage()
                            : "Unknown error getting image annotations");
        }
        return response.getLabelAnnotations();
        // [END parse_response]
    }

}
