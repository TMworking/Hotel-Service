package org.example.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ImportServiceTest {

    @InjectMocks
    private ImportService importService;

    @Test
    void shouldGetDataFromCsvAndReturnListWhenFileExistsTest() throws IOException, URISyntaxException {
        // given
        String filePath = "csv/test-import-csv.csv";
        Path importPath = Paths.get(getClass().getClassLoader().getResource(filePath).toURI());

        // when
        List<String> result = importService.getDataFromCsv(importPath.toString());

        // then
        assertNotNull(result, "Result shouldn't be null");
        assertEquals(3, result.size(), "Size of the list should be 3");
        assertTrue(result.contains("a"), "List should contain 'a'");
        assertTrue(result.contains("b"), "List should contain 'b'");
        assertTrue(result.contains("c"), "List should contain 'c'");
    }

    @Test
    void shouldNotGetDataFromCsvAndThrowIOExceptionWhenFileCannotBeReadTest() throws URISyntaxException, IOException {
        // given
        String filePath = "csv/test-import-csv.csv";
        Path importPath = Paths.get(getClass().getClassLoader().getResource(filePath).toURI());
        Files.deleteIfExists(importPath);

        // when
        // then
        assertThrows(IOException.class, () -> importService.getDataFromCsv(importPath.toString()),
                "Should throw IOException when the file does not exist");
    }
}
