package org.example.ui.actions.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.ExportService;
import org.example.service.domain.RoomService;
import org.example.ui.actions.RoomAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportRoomsAction implements RoomAction {

    private final RoomService roomService;
    private final ExportService exportService;
    @Value("${roomsPath}")
    private String roomsPath;

    @Override
    public void execute() {
        try {
            exportService.exportToCsv(roomService.getRooms(), roomsPath);
            System.out.println("Rooms successfully exported");
        } catch (IOException e) {
            System.err.println("Error exporting rooms: " + e.getMessage());
        }
    }
}
