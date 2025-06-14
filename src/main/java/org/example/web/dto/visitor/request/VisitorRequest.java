package org.example.web.dto.visitor.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 50, message = "Name must be greater than 1 and less than 50 characters")
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(min = 1, max = 50, message = "Surname must be greater than 1 and less than 50 characters")
    private String surname;

    @NotBlank(message = "Passport is required")
    @Size(max = 50, message = "Passport must be less than 50 characters")
    private String passport;
}
