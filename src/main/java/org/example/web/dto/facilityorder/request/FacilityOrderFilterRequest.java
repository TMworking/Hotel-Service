package org.example.web.dto.facilityorder.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityOrderFilterRequest {

    @Builder.Default
    String sortBy = "id";

    @Builder.Default
    String sortDirection = "asc";

    @Builder.Default
    int pageNumber = 0;

    @Builder.Default
    int pageSize = 20;
}
