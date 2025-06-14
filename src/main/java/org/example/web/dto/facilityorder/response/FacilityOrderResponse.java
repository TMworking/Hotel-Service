package org.example.web.dto.facilityorder.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityOrderResponse {
    private Long id;
    private Long facilityId;
    private Long visitorId;
    private LocalDateTime date;
}
