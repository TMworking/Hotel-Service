package org.example.web.dto.room.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enums.RoomStatus;
import org.example.model.enums.RoomType;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private Long id;
    private String roomNumber;
    private BigDecimal price;
    private RoomStatus status;
    private RoomType roomType;
}
