package org.example.service.web;

import org.example.web.dto.room.request.RoomFilterRequest;
import org.example.web.dto.room.request.RoomRequest;
import org.example.web.dto.room.response.RoomPageResponse;
import org.example.web.dto.room.response.RoomResponse;
import org.example.web.dto.visitor.response.VisitorResponse;
import org.example.model.enums.RoomStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WebRoomService {

    RoomPageResponse getAllRooms(RoomFilterRequest filter);

    ResponseEntity<String> changeRoomStatus(Long id, RoomStatus status);

    void exportRooms();

    RoomResponse getRoomsById(Long id);

    List<VisitorResponse> getRoomLastVisitors(Long id);

    ResponseEntity<String> saveRoom(RoomRequest request);
}
