package org.example.mappers;

import org.example.web.dto.booking.response.BookingPageResponse;
import org.example.web.dto.booking.response.BookingResponse;
import org.example.web.dto.booking.response.BookingShortResponse;
import org.example.model.domain.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "visitorId", source = "visitor.id")
    @Mapping(target = "roomId", source = "room.id")
    BookingResponse toResponse(Booking booking);

    BookingShortResponse toShortResponse(Booking booking);

    default BookingPageResponse toPageResponse(
            List<Booking> bookings,
            int pageNumber,
            int pageSize,
            long totalRecords
    ) {
      List<BookingShortResponse> content = bookings.stream()
              .map(this::toShortResponse)
              .toList();

      return new BookingPageResponse(content, pageNumber, pageSize, totalRecords);
    }
}
