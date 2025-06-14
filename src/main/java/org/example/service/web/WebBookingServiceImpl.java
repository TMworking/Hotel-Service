package org.example.service.web;

import lombok.RequiredArgsConstructor;
import org.example.web.dto.booking.request.BookingFilterRequest;
import org.example.web.dto.booking.response.BookingPageResponse;
import org.example.mappers.BookingMapper;
import org.example.web.dto.booking.response.BookingResponse;
import org.example.web.dto.booking.request.SettleRequest;
import org.example.exception.CsvExportException;
import org.example.model.domain.Booking;
import org.example.model.domain.Room;
import org.example.model.domain.Visitor;
import org.example.service.domain.BookingService;
import org.example.service.domain.ExportService;
import org.example.service.domain.RoomService;
import org.example.service.domain.VisitorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WebBookingServiceImpl implements WebBookingService {

    private final BookingService bookingService;
    private final RoomService roomService;
    private final VisitorService visitorService;
    private final ExportService exportService;
    private final BookingMapper bookingMapper;

    @Value("${bookingsTomcatExportPath}")
    private String bookingsExportPath;

    @Override
    public void exportBookings() {
        try {
            List<Booking> bookings = bookingService.getBookings();
            exportService.exportToCsv(bookings, bookingsExportPath);
        } catch (IOException e) {
            throw new CsvExportException("Failed to export bookings: " + e.getMessage());
        }
    }

    @Override
    public BookingPageResponse getAllBookings(BookingFilterRequest request) {
        List<Booking> bookings = bookingService.getBookings(request.getPageNumber(), request.getPageSize());
        long totalRecords = bookingService.countAll();
        return bookingMapper.toPageResponse(bookings, request.getPageNumber(), request.getPageSize(), totalRecords);
    }

    @Override
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingService.getById(id);
        return bookingMapper.toResponse(booking);
    }

    @Override
    public ResponseEntity<String> evictVisitorFromRoom(Long id) {
        Booking booking = bookingService.getById(id);
        bookingService.evictVisitorFromRoom(booking);
        return ResponseEntity.ok("Visitor successfully evicted from room.");
    }

    @Override
    public ResponseEntity<String> settleVisitorInRoom(SettleRequest request) {
        Room room = roomService.getById(request.getRoomId());
        Visitor visitor = visitorService.getById(request.getVisitorId());
        bookingService.settleVisitorInRoom(room, visitor, request.getDuration(), request.getSettleDate());
        return ResponseEntity.ok("Booking successfully saved. Visitor settled in room.");
    }
}
