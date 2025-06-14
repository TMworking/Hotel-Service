package org.example.web.dto.facilityorder.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityOrderRequest {
    @NotNull(message = "Visitor ID is required")
    private Long visitorId;

    @NotNull(message = "Facility ID is required")
    private Long facilityId;

    @NotNull(message = "Order date is required")
    private LocalDateTime orderDate;
}
