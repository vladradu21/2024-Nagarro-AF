package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.exception.CustomConflictException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ImageStorageService {
    private final Path movieImagePath;
    private final Path actorImagePath;

    public ImageStorageService(@Value("${variables.image.path.movies}") String moviePath,
                               @Value("${variables.image.path.actors}") String actorPath) {
        this.movieImagePath = Paths.get(moviePath);
        this.actorImagePath = Paths.get(actorPath);
    }

    public List<String> storeImages(List<MultipartFile> files, String entityType) throws IOException {
        List<String> storedFilesPaths = new ArrayList<>();
        Path storagePath = getStoragePath(entityType);
        ensureDirectoryExists(storagePath);

        for (MultipartFile file : files) {
            String fileName = validateFileName(file.getOriginalFilename());
            storeAndRecordFile(storagePath, fileName, file, storedFilesPaths);
        }

        return storedFilesPaths;
    }

    private Path getStoragePath(String entityType) {
        return entityType.equals("movie") ? movieImagePath : actorImagePath;
    }

    private void ensureDirectoryExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private String validateFileName(String fileName) {
        String cleanedFileName = StringUtils.cleanPath(Objects.requireNonNull(fileName));
        if (cleanedFileName.contains("..")) {
            throw new CustomConflictException("invalid path sequence " + cleanedFileName);
        }
        return cleanedFileName;
    }

    private void storeAndRecordFile(Path storagePath, String fileName, MultipartFile file, List<String> storedFilesPaths) throws IOException {
        Path destinationPath = storagePath.resolve(fileName);
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        storedFilesPaths.add(destinationPath.toString());
    }
}