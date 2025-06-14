package org.example.ui.actions.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Booking;
import org.example.model.domain.Visitor;
import org.example.service.domain.BookingService;
import org.example.service.domain.VisitorService;
import org.example.ui.actions.BookingAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EvictVisitorFromRoomAction implements BookingAction {

    private final BookingService bookingService;
    private final VisitorService visitorService;

    @Override
    public void execute() {
        try {
            System.out.println("visitors in hotel: ");
            System.out.println(visitorService.getVisitors());
            Long visitorId = UserInputs.inputId("Input visitor id: ");
            Visitor visitor = visitorService.getById(visitorId);

            System.out.println("There are visitors bookings: ");
            List<Booking> bookings = visitorService.getVisitorsBookings(visitor);
            System.out.println(bookings);

            Long bookingId = UserInputs.inputId("Input booking id, where evict visitor: ");
            Booking booking = bookingService.getById(bookingId);

            bookingService.evictVisitorFromRoom(booking);
        } catch (EntityNotFoundException e) {
            log.error("Failed to evict visitor: {}", e.getMessage(), e);
        }
    }
}
