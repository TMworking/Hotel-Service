package org.example.service.domain;

import org.example.model.domain.Exportable;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportService {

    public <T extends Exportable> void exportToCsv(List<T> entities, String filePath) throws IOException {

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }

        if (entities.isEmpty()) {
            return;
        }

        String header = entities.get(0).csvHeader();

        String csvContent = entities.stream()
                .map(T::toCsvString)
                .collect(Collectors.joining(System.lineSeparator()));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            writer.write(header);
            writer.newLine();
            writer.write(csvContent);
        }
    }
}

