package org.example.web.dto.facilityorder.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityOrderPageResponse {
    private List<FacilityOrderShortResponse> bookings;
    private int pageNumber;
    private int pageSize;
    private long totalRecords;
}
