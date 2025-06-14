package org.example.ui.actions.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Room;
import org.example.model.domain.Visitor;
import org.example.service.domain.BookingService;
import org.example.service.domain.RoomService;
import org.example.service.domain.VisitorService;
import org.example.ui.actions.BookingAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SettleVisitorInRoomAction implements BookingAction {

    private final BookingService bookingService;
    private final RoomService roomService;
    private final VisitorService visitorService;

    @Override
    public void execute() {
        try {
            LocalDateTime settleDate = UserInputs.inputDate("Input settle date by yyyy-MM-dd format: ");
            Integer duration = UserInputs.inputDuration("Input days duration: ");

            List<Room> roomList = roomService.getFreeRoomsByDate(settleDate);

            if (roomList.isEmpty()) {
                System.out.println("There are no free rooms on this date");
            } else {
                System.out.println("Free rooms on this date: " + roomList);

                String roomNumber = UserInputs.inputString("Input room number: ");
                Room room = roomService.getByNumber(roomNumber);

                Long visitorId = UserInputs.inputId("Input visitor id: ");
                Visitor visitor = visitorService.getById(visitorId);

                bookingService.settleVisitorInRoom(room, visitor, duration, settleDate);
            }
        } catch (EntityNotFoundException e) {
            log.error("Failed to settle visitor: {}", e.getMessage(), e);
        }
    }
}
