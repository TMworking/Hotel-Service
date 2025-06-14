package org.example.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class SerializeServiceTest {

    @InjectMocks
    private SerializeService serializeService;

    @Test
    void shouldExportToJsonAndWriteContentWhenJsonProvidedTest() throws IOException, URISyntaxException {
        // given
        String json = "{\"key\": \"value\"}";
        String filePath = "data/test-export.json";
        Path importPath = Paths.get(getClass().getClassLoader().getResource(filePath).toURI());

        // when
        serializeService.exportToJson(json, importPath.toString());

        // then
        String content = Files.readString(importPath);
        assertEquals(json, content, "File content should match with JSON");
    }

    @Test
    void shouldExportToJsonAndNotWriteWhenJsonIsEmptyTest() throws IOException, URISyntaxException {
        // given
        String json = "";
        String filePath = "data/test-export.json";
        Path importPath = Paths.get(getClass().getClassLoader().getResource(filePath).toURI());

        // when
        serializeService.exportToJson(json, importPath.toString());

        // then
        String content = Files.readString(importPath);
        assertEquals("", content, "File should be empty when empty JSON is provided");
    }
}
