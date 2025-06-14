package org.example.service.web;

import lombok.RequiredArgsConstructor;
import org.example.web.dto.room.request.RoomFilterRequest;
import org.example.web.dto.room.response.RoomPageResponse;
import org.example.mappers.RoomMapper;
import org.example.web.dto.room.request.RoomRequest;
import org.example.web.dto.room.response.RoomResponse;
import org.example.mappers.VisitorMapper;
import org.example.web.dto.visitor.response.VisitorResponse;
import org.example.exception.CsvExportException;
import org.example.model.domain.Room;
import org.example.model.enums.RoomStatus;
import org.example.service.domain.ExportService;
import org.example.service.domain.RoomService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebRoomServiceImpl implements WebRoomService {

    private final RoomService roomService;
    private final ExportService exportService;
    private final RoomMapper roomMapper;
    private final VisitorMapper visitorMapper;

    @Value("${roomsTomcatExportPath}")
    private String roomsExportPath;

    @Override
    public RoomResponse getRoomsById(Long id) {
        return roomMapper.toResponse(roomService.getById(id));
    }

    @Override
    public RoomPageResponse getAllRooms(RoomFilterRequest filter) {
        List<Room> rooms = roomService.getRooms(filter);
        long totalRecords = roomService.countAll();
        return roomMapper.toPageResponse(rooms, filter.getPage(), filter.getSize(), totalRecords);
    }

    @Override
    public ResponseEntity<String> changeRoomStatus(Long id, RoomStatus status) {
        roomService.changeRoomStatus(id, status);
        return ResponseEntity.ok("Room status successfully changed");
    }

    @Override
    public void exportRooms() {
        try {
            List<Room> rooms = roomService.getRooms();
            exportService.exportToCsv(rooms, roomsExportPath);
        } catch (IOException e) {
            throw new CsvExportException("Failed to export rooms: " + e.getMessage());
        }
    }

    @Override
    public List<VisitorResponse> getRoomLastVisitors(Long id) {
        Room room = roomService.getById(id);
        return visitorMapper.toResponseList(roomService.getLastRoomVisitors(room));
    }

    @Override
    public ResponseEntity<String> saveRoom(RoomRequest request) {
        roomService.saveRoom(request.getRoomNumber(), request.getCost(), request.getRoomType());
        return ResponseEntity.ok("Room successfully saved");
    }
}
