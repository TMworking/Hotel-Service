package org.example.mappers;

import org.example.model.domain.User;
import org.example.web.dto.user.response.UserPageResponse;
import org.example.web.dto.user.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role.roleName")
    UserResponse toResponse(User user);

    default UserPageResponse toPageResponse(
            List<User> users,
            int pageNumber,
            int pageSize,
            long totalRecords
    ) {
        List<UserResponse> content = users.stream()
                .map(this::toResponse)
                .toList();

        return new UserPageResponse(content, pageNumber, pageSize, totalRecords);
    }
}
