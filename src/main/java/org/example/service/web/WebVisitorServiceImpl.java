package org.example.service.web;

import lombok.RequiredArgsConstructor;
import org.example.web.dto.visitor.request.VisitorFilterRequest;
import org.example.web.dto.visitor.request.VisitorRequest;
import org.example.web.dto.visitor.response.VisitorPageResponse;
import org.example.mappers.VisitorMapper;
import org.example.web.dto.visitor.response.VisitorResponse;
import org.example.exception.CsvExportException;
import org.example.model.domain.Visitor;
import org.example.service.domain.ExportService;
import org.example.service.domain.VisitorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebVisitorServiceImpl implements WebVisitorService {

    private final VisitorService visitorService;
    private final VisitorMapper visitorMapper;
    private final ExportService exportService;

    @Value("${visitorsTomcatExportPath}")
    private String visitorsExportPath;

    @Override
    public void exportVisitors() {
        try {
            List<Visitor> visitors = visitorService.getVisitors();
            exportService.exportToCsv(visitors, visitorsExportPath);
        } catch (IOException e) {
            throw new CsvExportException("Failed to export visitors: " + e.getMessage());
        }
    }

    @Override
    public VisitorPageResponse getAllVisitors(VisitorFilterRequest request) {
        List<Visitor> visitors = visitorService.getVisitors(request.getPageNumber(), request.getPageSize(), request.getSortBy(), request.getSortDirection());
        long totalRecords = visitorService.countAll();
        return visitorMapper.toPageResponse(visitors, request.getPageNumber(), request.getPageSize(), totalRecords);
    }

    @Override
    public VisitorResponse getVisitorById(Long id) {
        Visitor visitor = visitorService.getById(id);
        return visitorMapper.toResponse(visitor);
    }

    @Override
    public ResponseEntity<String> saveVisitor(VisitorRequest visitorRequest) {
        visitorService.saveVisitor(visitorRequest.getName(), visitorRequest.getSurname(), visitorRequest.getPassport());
        return ResponseEntity.ok("Visitor successfully saved");
    }
}
