package com.nagarro.af24.cinema.service;

import com.nagarro.af24.cinema.utils.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ImageStorageServiceTest {
    @Test
    void testStoreImages(@TempDir Path tempDir) {
        List<MultipartFile> files = TestData.getMockMultipartFiles();
        String entityType = "movie";
        Path movieImagePath = tempDir.resolve("movie");
        Path actorImagePath = tempDir.resolve("actor");
        ImageStorageService imageStorageService = new ImageStorageService(movieImagePath.toString(), actorImagePath.toString());

        List<String> storedFilesPaths = imageStorageService.storeImages(files, entityType);

        Assertions.assertEquals(files.size(), storedFilesPaths.size());
    }
}