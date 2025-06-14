package org.example.web.dto.visitor.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorPageResponse {
    private List<VisitorShortResponse> bookings;
    private int pageNumber;
    private int pageSize;
    private long totalRecords;
}
