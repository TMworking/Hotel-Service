package org.example.web.dto.booking.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long visitorId;
    private Long roomId;
    private LocalDateTime settleDate;
    private Integer duration;
}
