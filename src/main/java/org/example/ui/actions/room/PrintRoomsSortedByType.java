package org.example.ui.actions.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.RoomService;
import org.example.ui.actions.RoomAction;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintRoomsSortedByType implements RoomAction {

    private final RoomService roomService;

    @Override
    public void execute() {
        System.out.println(roomService.getRoomsSortedByType());
    }
}
