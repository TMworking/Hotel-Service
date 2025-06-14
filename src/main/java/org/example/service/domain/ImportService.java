package org.example.service.domain;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class ImportService {

    public List<String> getDataFromCsv(String filePath) throws IOException {
            try (BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(filePath)))) {
                return reader.lines().toList();
            }
    }
}
