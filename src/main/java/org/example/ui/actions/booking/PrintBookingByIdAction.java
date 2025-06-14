package org.example.ui.actions.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.EntityNotFoundException;
import org.example.service.domain.BookingService;
import org.example.ui.actions.BookingAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintBookingByIdAction implements BookingAction {

    private final BookingService bookingService;

    @Override
    public void execute() {
        try {
            Long entityId = UserInputs.inputId("Input entity id: ");
            System.out.println(bookingService.getById(entityId));
        } catch (EntityNotFoundException e) {
            log.error("Failed to find booking: {}", e.getMessage(), e);
        }
    }
}
