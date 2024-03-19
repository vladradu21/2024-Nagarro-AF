package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.exception.CustomConflictException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class ImageStorageService {

    private static final Path MOVIE_IMAGE_PATH = Paths.get("D:/nagarro/images/movies");
    private static final Path ACTOR_IMAGE_PATH = Paths.get("D:/nagarro/images/actors");

    public String storeImage(MultipartFile file, String entityType) throws IOException {
        Path storagePath = entityType.equals("movie") ? MOVIE_IMAGE_PATH : ACTOR_IMAGE_PATH;

        if(!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if(fileName.contains("..")) {
            throw new CustomConflictException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        Path destinationPath = storagePath.resolve(fileName);
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        return destinationPath.toString();
    }
}