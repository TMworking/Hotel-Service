package org.example.service.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeserializeService {

    public String getDataFromJson(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            log.debug("Backup json got from: {}", path);
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
