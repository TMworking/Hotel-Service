package org.example.mappers;

import org.example.web.dto.room.response.RoomPageResponse;
import org.example.web.dto.room.response.RoomResponse;
import org.example.model.domain.Room;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomResponse toResponse(Room room);

    default RoomPageResponse toPageResponse(
            List<Room> rooms,
            int pageNumber,
            int pageSize,
            long totalRecords
    ) {
        List<RoomResponse> content = rooms.stream()
                .map(this::toResponse)
                .toList();

        return new RoomPageResponse(content, pageNumber, pageSize, totalRecords);
    }
}
