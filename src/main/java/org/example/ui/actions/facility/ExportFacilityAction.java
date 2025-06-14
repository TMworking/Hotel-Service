package org.example.ui.actions.facility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.ExportService;
import org.example.service.domain.FacilityService;
import org.example.ui.actions.FacilityAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportFacilityAction implements FacilityAction {

    private final FacilityService facilityService;
    private final ExportService exportService;
    @Value("${facilitiesPath}")
    private String facilitiesPath;

    @Override
    public void execute() {
        try {
            exportService.exportToCsv(facilityService.getFacilities(), facilitiesPath);
            System.out.println("Facilities successfully exported");
        } catch (IOException e) {
            log.error("Error exporting facilities to CSV: {}", e.getMessage(), e);
        }
    }
}
