package org.example.web.dto.booking.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingFilterRequest {

    @Builder.Default
    int pageNumber = 0;

    @Builder.Default
    int pageSize = 20;
}
