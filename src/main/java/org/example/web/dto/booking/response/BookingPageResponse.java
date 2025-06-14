package org.example.web.dto.booking.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingPageResponse {
    private List<BookingShortResponse> bookings;
    private int pageNumber;
    private int pageSize;
    private long totalRecords;
}
