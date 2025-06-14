package org.example.web.dto.facility.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enums.FacilityType;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityResponse {
    private Long id;
    private BigDecimal cost;
    private FacilityType facilityType;
}
