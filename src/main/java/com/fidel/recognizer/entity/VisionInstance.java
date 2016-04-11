package com.fidel.recognizer.entity;


import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.*;
import com.google.api.services.vision.v1.model.Image;
import com.google.common.collect.ImmutableList;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class VisionInstance {

    private final Vision vision;

    /**
     * Constructs a {@link VisionInstance} which connects to the Vision API.
     */
    public VisionInstance (Vision vision) {
        this.vision = vision;
    }

    // [START detect_face]
    /**
     * Gets up to {@code maxResults} faces for an image stored at {@code path}.
     */
    public List<FaceAnnotation> detectFaces(Path path, int maxResults) throws IOException {
        byte[] data = Files.readAllBytes(path);
        System.out.println("data = " + data.length);

        AnnotateImageRequest request =
                new AnnotateImageRequest()
                        .setImage(new Image().encodeContent(data))
                        .setFeatures(ImmutableList.of(
                                new Feature()
                                        .setType("FACE_DETECTION")
                                        .setMaxResults(maxResults)));
        System.out.println("request = " + request);

        Vision.Images.Annotate annotate =
                vision.images()
                        .annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotate.setDisableGZipContent(true);

        System.out.println("ImmutableList.of(request) = " + ImmutableList.of(request));
        System.out.println("annotate = " + annotate);

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

    // [START highlight_faces]
    /**
     * Reads image {@code inputPath} and writes {@code outputPath} with {@code faces} outlined.
     */
    public void writeWithFaces(Path inputPath, Path outputPath, List<FaceAnnotation> faces)
            throws IOException {
        BufferedImage img = ImageIO.read(inputPath.toFile());
        annotateWithFaces(img, faces);
        ImageIO.write(img, "jpg", outputPath.toFile());
    }

    /**
     * Annotates an image {@code img} with a polygon around each face in {@code faces}.
     */
    public void annotateWithFaces(BufferedImage img, List<FaceAnnotation> faces) {
        for (FaceAnnotation face : faces) {
            annotateWithFace(img, face);
        }
    }

    /**
     * Annotates an image {@code img} with a polygon defined by {@code face}.
     */
    private void annotateWithFace(BufferedImage img, FaceAnnotation face) {
        Graphics2D gfx = img.createGraphics();
        Polygon poly = new Polygon();
        for (Vertex vertex : face.getFdBoundingPoly().getVertices()) {
            poly.addPoint(vertex.getX(), vertex.getY());
        }
        gfx.setStroke(new BasicStroke(2));
        gfx.setColor(new java.awt.Color(0xffff00));
        gfx.draw(poly);
    }
    // [END highlight_faces]
}
