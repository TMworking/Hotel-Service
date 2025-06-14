package org.example.web.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enums.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleRequest {
    @NotNull
    UserRole role;
}
