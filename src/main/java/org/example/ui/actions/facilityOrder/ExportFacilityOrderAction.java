package org.example.ui.actions.facilityOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.ExportService;
import org.example.service.domain.FacilityOrderService;
import org.example.ui.actions.FacilityOrderAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportFacilityOrderAction implements FacilityOrderAction {

    private final FacilityOrderService facilityOrderService;
    private final ExportService exportService;
    @Value("${facilityOrdersPath}")
    private String facilityOrdersPath;

    @Override
    public void execute() {
        try {
            exportService.exportToCsv(facilityOrderService.getFacilityOrders(), facilityOrdersPath);
            System.out.println("Facility orders successfully exported");
        } catch (IOException e) {
            log.error("Error exporting facility orders: {}", e.getMessage(), e);
        }
    }
}
