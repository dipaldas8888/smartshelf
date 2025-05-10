package com.dipal.smartshelf.controller;

import org.apache.coyote.BadRequestException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class FileUploadController {

    @Value("${application.image.upload-dir}")
    private String imageUploadDir;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws BadRequestException, FileUploadException {
        // Validate file type
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new BadRequestException("Invalid file type. Only image files are allowed.");
        }

        // Generate safe filename
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = UUID.randomUUID() + "_" + filename;

        // Save file
        Path uploadPath = Paths.get(imageUploadDir).toAbsolutePath().normalize();
        Path targetLocation = uploadPath.resolve(uniqueFilename);
        try {
            Files.createDirectories(uploadPath);
            try {
                file.transferTo(targetLocation);
            } catch (IOException ex) {
                throw new FileUploadException("Failed to save file to storage", ex);
            }
        } catch (IOException ex) {
            throw new FileUploadException("Failed to create upload directory", ex);
        }
        return ResponseEntity.ok(uniqueFilename);
    }
}