package org.example.web.dto.room.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomPageResponse {
    private List<RoomResponse> bookings;
    private int pageNumber;
    private int pageSize;
    private long totalRecords;
}
