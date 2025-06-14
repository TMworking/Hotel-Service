package org.example.ui.actions.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.EntityNotFoundException;
import org.example.service.domain.RoomService;
import org.example.ui.actions.RoomAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintRoomByIdAction implements RoomAction {

    private final RoomService roomService;

    @Override
    public void execute() {
        try {
            Long entityId = UserInputs.inputId("Input entity id: ");
            System.out.println(roomService.getById(entityId));
        } catch (EntityNotFoundException e) {
            log.error("Failed to find room: {}", e.getMessage(), e);
        }
    }
}
