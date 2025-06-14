package org.example.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.service.web.WebUserService;
import org.example.web.dto.user.request.UserRoleRequest;
import org.example.web.dto.user.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final WebUserService userWebService;

    @GetMapping("/export")
    public ResponseEntity<Void> exportUsers() {
        userWebService.exportUsers();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/{id}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable("id") Long id, @Valid @RequestBody UserRoleRequest request) {
        userWebService.updateUserRole(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userWebService.findUserById(id));
    }

    @GetMapping("/{id}/deactivation")
    public ResponseEntity<Void> deactivateUser(@PathVariable("id") Long id) {
        userWebService.deactivateUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
