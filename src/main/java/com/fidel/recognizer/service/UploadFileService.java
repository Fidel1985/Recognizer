package com.fidel.recognizer.service;

import com.fidel.recognizer.entity.UploadItem;

import java.io.IOException;

public interface UploadFileService {
    String uploadFile(UploadItem uploadItem) throws IOException;
}
