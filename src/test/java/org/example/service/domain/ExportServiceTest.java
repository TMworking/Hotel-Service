package org.example.service.domain;

import org.example.model.domain.Exportable;
import org.example.utils.TestObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExportServiceTest {

    @InjectMocks
    private ExportService exportService;

    static Stream<Arguments> exportEntitiesTestCases() {
        return Stream.of(
                Arguments.of(TestObjectUtils.createTestRoom()),
                Arguments.of(TestObjectUtils.createTestVisitor()),
                Arguments.of(TestObjectUtils.createTestbooking()),
                Arguments.of(TestObjectUtils.createTestFacility()),
                Arguments.of(TestObjectUtils.createTestFacilityOrder())
        );
    }

    @ParameterizedTest
    @MethodSource("exportEntitiesTestCases")
    void shouldExportToCsvAndWriteDataWhenValidEntitiesTest(Exportable entity) throws IOException, URISyntaxException {
        // given
        List<Exportable> entitiesList = Collections.singletonList(entity);
        String filePath = "csv/test-export-csv.csv";
        Path exportPath = Paths.get(getClass().getClassLoader().getResource(filePath).toURI());

        // when
        exportService.exportToCsv(entitiesList, exportPath.toString());

        // then
        List<String> lines = Files.readAllLines(exportPath);
        assertEquals(2, lines.size(), "CSV should have header and data line");
        assertEquals(entity.csvHeader(), lines.get(0), "First line should be header");
        assertEquals(entity.toCsvString(), lines.get(1), "Third line should contain data");
    }

    @Test
    void shouldNotExportToCsvAndWriteWhenEntitiesListIsEmpty() throws IOException, URISyntaxException {
        // given
        List<Exportable> bookings = Collections.emptyList();
        String filePath = "csv/test-export-csv.csv";
        Path exportPath = Paths.get(getClass().getClassLoader().getResource(filePath).toURI());

        // when
        exportService.exportToCsv(bookings, exportPath.toString());

        // then
        List<String> lines = Files.readAllLines(exportPath);
        assertEquals(0, lines.size(), "File should be empty for empty list");
    }
}
