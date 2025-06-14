package org.example.web.dto.facility.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityPageResponse {
    private List<FacilityResponse> bookings;
    private int pageNumber;
    private int pageSize;
    private long totalRecords;
}
