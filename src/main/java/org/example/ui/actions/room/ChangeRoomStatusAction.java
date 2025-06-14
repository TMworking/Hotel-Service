package org.example.ui.actions.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Room;
import org.example.model.enums.RoomStatus;
import org.example.service.domain.RoomService;
import org.example.ui.actions.RoomAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChangeRoomStatusAction implements RoomAction {

    private final RoomService roomService;

    @Override
    public void execute() {
        try {
            System.out.println(roomService.getRooms());
            Long roomId = UserInputs.inputId("Input room id: ");
            Room room = roomService.getById(roomId);

            RoomStatus roomStatus = UserInputs.inputRoomStatus("Input room status (FREE, REPAIRED, SERVICED):");
            room.setStatus(roomStatus);

            roomService.changeRoomStatus(roomId, roomStatus);
        } catch (EntityNotFoundException e) {
            log.error("Failed to find room: {}", e.getMessage(), e);
        } catch (IllegalStateException e) {
            log.error("Error changing room status: {}", e.getMessage(), e);
        }
    }
}
