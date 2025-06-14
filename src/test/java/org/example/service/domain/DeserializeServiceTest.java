package org.example.service.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DeserializeServiceTest {

    @InjectMocks
    private DeserializeService deserializeService;

    @Test
    void shouldGetDataFromJsonAndReturnFileContentWhenFileExistsTest() throws IOException, URISyntaxException {
        // given
        String expectedContent = "{ \"name\" : \"test\", \"value\" : 123 }";
        String filePath = "data/test-import.json";

        // when
        String actualContent = deserializeService.getDataFromJson(
                Paths.get(getClass().getClassLoader().getResource(filePath).toURI()).toString());

        // then
        assertNotNull(actualContent, "Result shouldn't be null");
        assertEquals(expectedContent, actualContent, "File content should match expected");
    }

    @Test
    void ShouldNotGetDataFromJsonAndThrowIOExceptionWhenFileDoesNotExistTest() {
        // given
        String nonExistentPath = "data/bad-file.json";

        // when
        // then
        assertThrows(IOException.class, () -> deserializeService.getDataFromJson(nonExistentPath));
    }
}
