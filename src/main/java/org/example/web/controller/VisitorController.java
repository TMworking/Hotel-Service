package org.example.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.web.dto.visitor.request.VisitorFilterRequest;
import org.example.web.dto.visitor.request.VisitorRequest;
import org.example.web.dto.visitor.response.VisitorPageResponse;
import org.example.web.dto.visitor.response.VisitorResponse;
import org.example.service.web.WebVisitorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/visitors")
@RequiredArgsConstructor
public class VisitorController {

    private final WebVisitorService webVisitorService;

    @GetMapping("/export")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> exportVisitors() {
        webVisitorService.exportVisitors();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/all")
    public ResponseEntity<VisitorPageResponse> getAllVisitors(@Valid @RequestBody VisitorFilterRequest request) {
        VisitorPageResponse visitorResponses = webVisitorService.getAllVisitors(request);
        return ResponseEntity.ok(visitorResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitorResponse> getVisitorById(@PathVariable("id") Long id) {
        VisitorResponse response = webVisitorService.getVisitorById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> saveVisitor(@Valid VisitorRequest visitorRequest) {
        return webVisitorService.saveVisitor(visitorRequest);
    }
}
