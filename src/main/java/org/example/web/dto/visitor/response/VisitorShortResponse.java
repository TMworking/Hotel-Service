package org.example.web.dto.visitor.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorShortResponse {
    private Long id;
    private String name;
    private String surname;
}
