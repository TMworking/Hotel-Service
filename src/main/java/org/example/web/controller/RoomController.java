package org.example.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.web.dto.room.request.RoomFilterRequest;
import org.example.web.dto.room.request.RoomRequest;
import org.example.web.dto.room.response.RoomPageResponse;
import org.example.web.dto.room.response.RoomResponse;
import org.example.web.dto.visitor.response.VisitorResponse;
import org.example.model.enums.RoomStatus;
import org.example.service.web.WebRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final WebRoomService webRoomService;

    @GetMapping("/export")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> exportRooms() {
        webRoomService.exportRooms();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(webRoomService.getRoomsById(id));
    }

    @PostMapping("/all")
    public ResponseEntity<RoomPageResponse> getAllRooms(
            @Valid @RequestBody RoomFilterRequest filter) {
        return ResponseEntity.ok(webRoomService.getAllRooms(filter));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> changeRoomStatusById(@PathVariable("id") Long id, @RequestParam RoomStatus status) {
        return webRoomService.changeRoomStatus(id, status);
    }

    @GetMapping("/{id}/last-visitors")
    public ResponseEntity<List<VisitorResponse>> getRoomLastVisitors(@PathVariable("id") Long id) {
        List<VisitorResponse> visitorResponses = webRoomService.getRoomLastVisitors(id);
        return ResponseEntity.ok(visitorResponses);
    }

    @PostMapping
    public ResponseEntity<String> createRoom(@Valid @RequestBody RoomRequest request) {
        return webRoomService.saveRoom(request);
    }
}
