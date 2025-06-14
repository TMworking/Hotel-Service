package org.example.mappers;

import org.example.web.dto.visitor.response.VisitorPageResponse;
import org.example.web.dto.visitor.response.VisitorResponse;
import org.example.web.dto.visitor.response.VisitorShortResponse;
import org.example.model.domain.Visitor;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VisitorMapper {

    VisitorResponse toResponse(Visitor visitor);

    VisitorShortResponse toShortResponse(Visitor visitor);

    default VisitorPageResponse toPageResponse(
            List<Visitor> visitors,
            int pageNumber,
            int pageSize,
            long totalRecords
    ) {
        List<VisitorShortResponse> content = visitors.stream()
                .map(this::toShortResponse)
                .toList();

        return new VisitorPageResponse(content, pageNumber, pageSize, totalRecords);
    }

    default List<VisitorResponse> toResponseList(List<Visitor> visitors) {
        return visitors.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
