package org.example.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.web.dto.booking.request.BookingFilterRequest;
import org.example.web.dto.booking.response.BookingPageResponse;
import org.example.web.dto.booking.response.BookingResponse;
import org.example.web.dto.booking.request.SettleRequest;
import org.example.service.web.WebBookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final WebBookingService webBookingService;

    @GetMapping("/export")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> exportBookings() {
        webBookingService.exportBookings();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping
    public ResponseEntity<String> createBooking(@Valid @RequestBody SettleRequest request) {
        return webBookingService.settleVisitorInRoom(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseEntity<String>> evictVisitorFromRoom(@PathVariable("id") Long id) {
        ResponseEntity<String> response = webBookingService.evictVisitorFromRoom(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/all")
    public ResponseEntity<BookingPageResponse> getAllBookings(BookingFilterRequest request) {
        BookingPageResponse bookingResponses = webBookingService.getAllBookings(request);
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable("id") Long id) {
        BookingResponse response = webBookingService.getBookingById(id);
        return ResponseEntity.ok(response);
    }
}
