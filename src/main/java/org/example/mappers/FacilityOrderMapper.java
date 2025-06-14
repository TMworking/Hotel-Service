package org.example.mappers;

import org.example.web.dto.facilityorder.response.FacilityOrderPageResponse;
import org.example.web.dto.facilityorder.response.FacilityOrderResponse;
import org.example.web.dto.facilityorder.response.FacilityOrderShortResponse;
import org.example.model.domain.FacilityOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacilityOrderMapper {

    @Mapping(target = "facilityId", source = "facility.id")
    @Mapping(target = "visitorId", source = "visitor.id")
    FacilityOrderResponse toResponse(FacilityOrder facilityOrder);

    FacilityOrderShortResponse toShortResponse(FacilityOrder facilityOrder);

    default FacilityOrderPageResponse toPageResponse(
            List<FacilityOrder> facilityOrders,
            int pageNumber,
            int pageSize,
            long totalRecords
    ) {
        List<FacilityOrderShortResponse> content = facilityOrders.stream()
                .map(this::toShortResponse)
                .toList();

        return new FacilityOrderPageResponse(content, pageNumber, pageSize, totalRecords);
    }
}
