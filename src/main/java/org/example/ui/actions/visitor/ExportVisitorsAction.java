package org.example.ui.actions.visitor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.ExportService;
import org.example.service.domain.VisitorService;
import org.example.ui.actions.VisitorAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportVisitorsAction implements VisitorAction {

    private final VisitorService visitorService;
    private final ExportService exportService;
    @Value("${visitorsPath}")
    private String visitorsPath;

    @Override
    public void execute() {
        try {
            exportService.exportToCsv(visitorService.getVisitors(), visitorsPath);
            log.debug("Visitors successfully exported");
        } catch (IOException e) {
            log.error("Error exporting visitors: ", e);
        }
    }
}
