package org.example.web.dto.booking.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettleRequest {

    @NotNull(message = "Settle date is required")
    private LocalDateTime settleDate;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration can't be less than 1 day")
    private Integer duration;

    @NotNull(message = "Room id is required")
    private Long roomId;

    @NotNull(message = "Visitor id is required")
    private Long visitorId;
}
