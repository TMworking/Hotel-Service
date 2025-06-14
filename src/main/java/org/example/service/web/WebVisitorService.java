package org.example.service.web;

import org.example.web.dto.visitor.request.VisitorFilterRequest;
import org.example.web.dto.visitor.request.VisitorRequest;
import org.example.web.dto.visitor.response.VisitorPageResponse;
import org.example.web.dto.visitor.response.VisitorResponse;
import org.springframework.http.ResponseEntity;

public interface WebVisitorService {

    void exportVisitors();

    VisitorPageResponse getAllVisitors(VisitorFilterRequest request);

    VisitorResponse getVisitorById(Long id);

    ResponseEntity<String> saveVisitor(VisitorRequest visitorRequest);
}
