package org.example.mappers;

import org.example.web.dto.facility.response.FacilityPageResponse;
import org.example.web.dto.facility.response.FacilityResponse;
import org.example.model.domain.Facility;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacilityMapper {

    FacilityResponse toResponse(Facility facility);

    default FacilityPageResponse toPageResponse(
            List<Facility> facilities,
            int pageNumber,
            int pageSize,
            long totalRecords
    ) {
        List<FacilityResponse> content = facilities.stream()
                .map(this::toResponse)
                .toList();

        return new FacilityPageResponse(content, pageNumber, pageSize, totalRecords);
    }
}
