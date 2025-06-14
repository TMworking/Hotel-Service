package org.example.ui.actions.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.BookingService;
import org.example.ui.actions.BookingAction;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintAllBookingsAction implements BookingAction {

    private final BookingService bookingService;

    @Override
    public void execute() {
        System.out.println(bookingService.getBookings());
    }
}
