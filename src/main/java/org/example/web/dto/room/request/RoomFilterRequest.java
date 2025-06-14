package org.example.web.dto.room.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.enums.RoomStatus;
import org.example.model.enums.RoomType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomFilterRequest {
    private RoomStatus status;
    private RoomType roomType;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    @Builder.Default
    private String sortBy = "id";
    @Builder.Default
    private String sortDirection = "ASC";

    @Builder.Default
    private int page = 0;
    @Builder.Default
    private int size = 10;

    @Builder.Default
    private LocalDateTime settleDate = LocalDateTime.now();
    private Integer duration;
}
