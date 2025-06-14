package org.example.web.dto.visitor.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitorFilterRequest {

    @Builder.Default
    String sortBy = "id";

    @Builder.Default
    String sortDirection = "asc";

    @Builder.Default
    int pageNumber = 0;

    @Builder.Default
    int pageSize = 20;
}
