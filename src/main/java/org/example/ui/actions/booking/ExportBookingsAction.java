package org.example.ui.actions.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.BookingService;
import org.example.service.domain.ExportService;
import org.example.ui.actions.BookingAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportBookingsAction implements BookingAction {

    private final BookingService bookingService;
    private final ExportService exportService;
    @Value("${bookingsPath}")
    private String bookingsPath;

    @Override
    public void execute() {
        try {
            exportService.exportToCsv(bookingService.getBookings(), bookingsPath);
            log.info("Bookings successfully exported to: {}", bookingsPath);
        } catch (IOException e) {
            log.error("Error exporting bookings to CSV: {}", e.getMessage(), e);
        }
    }
}
