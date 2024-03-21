package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.exception.CustomConflictException;
import com.nagarro.af24.cinema.exception.CustomStorageException;
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

    public List<String> storeImages(List<MultipartFile> files, String entityType) {
        try {
            List<String> storedFilesPaths = new ArrayList<>();
            Path storagePath = getStoragePath(entityType);
            ensureDirectoryExists(storagePath);
            processFiles(files, storagePath, storedFilesPaths);
            return storedFilesPaths;
        } catch (IOException e) {
            throw new CustomStorageException("Failed to store images");
        }
    }

    private void processFiles(List<MultipartFile> files, Path storagePath, List<String> storedFilesPaths) throws IOException {
        for (MultipartFile file : files) {
            String fileName = validateAndCleanFileName(file.getOriginalFilename());
            Path destinationPath = storeFile(storagePath, fileName, file);
            recordStoredFilePath(destinationPath, storedFilesPaths);
        }
    }

    private Path storeFile(Path storagePath, String fileName, MultipartFile file) throws IOException {
        Path destinationPath = storagePath.resolve(fileName);
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
        return destinationPath;
    }

    private void recordStoredFilePath(Path filePath, List<String> storedFilesPaths) {
        storedFilesPaths.add(filePath.toString());
    }

    private Path getStoragePath(String entityType) {
        return switch (entityType) {
            case "movie" -> movieImagePath;
            case "actor" -> actorImagePath;
            default -> throw new CustomConflictException("invalid entity type " + entityType);
        };
    }

    private void ensureDirectoryExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private String validateAndCleanFileName(String fileName) {
        String cleanedFileName = StringUtils.cleanPath(Objects.requireNonNull(fileName));
        if (cleanedFileName.contains("..")) {
            throw new CustomConflictException("invalid path sequence " + cleanedFileName);
        }
        return cleanedFileName;
    }
}