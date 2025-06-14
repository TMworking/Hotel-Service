package org.example.ui.actions.room;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.enums.RoomType;
import org.example.service.domain.RoomService;
import org.example.ui.actions.RoomAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomCreateAction implements RoomAction {

    private final RoomService roomService;

    @Override
    public void execute() {
        RoomType roomType = UserInputs.inputRoomType("Input room type (BASIC, DELUXE, PRESIDENT): ");
        String roomNumber = UserInputs.inputString("Input room number: ");
        BigDecimal cost = UserInputs.inputCost("Input room cost: ");

        roomService.saveRoom(roomNumber, cost, roomType);
    }
}
