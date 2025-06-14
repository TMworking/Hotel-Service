package org.example.service.web;

import org.example.web.dto.booking.request.BookingFilterRequest;
import org.example.web.dto.booking.response.BookingPageResponse;
import org.example.web.dto.booking.response.BookingResponse;
import org.example.web.dto.booking.request.SettleRequest;
import org.springframework.http.ResponseEntity;

public interface WebBookingService {

    void exportBookings();

    BookingPageResponse getAllBookings(BookingFilterRequest request);

    BookingResponse getBookingById(Long id);

    ResponseEntity<String> evictVisitorFromRoom(Long id);

    ResponseEntity<String> settleVisitorInRoom(SettleRequest request);
}
