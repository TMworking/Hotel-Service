package org.example.ui.actions.room;

import lombok.RequiredArgsConstructor;
import org.example.model.domain.Room;
import org.example.service.domain.RoomService;
import org.example.ui.actions.RoomAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PrintFreeRoomsOnDateAction implements RoomAction {

    private final RoomService roomService;

    @Override
    public void execute() {
        LocalDateTime settleDate = UserInputs.inputDate("Input settle date by yyyy-MM-dd format: ");
        List<Room> roomList = roomService.getFreeRoomsByDate(settleDate);
        System.out.println("Free rooms on this date: " + roomList);
    }
}
