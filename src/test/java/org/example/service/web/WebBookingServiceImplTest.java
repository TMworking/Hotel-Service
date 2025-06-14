package org.example.service.web;

import org.example.mappers.BookingMapper;
import org.example.model.domain.Booking;
import org.example.model.domain.Room;
import org.example.model.domain.Visitor;
import org.example.model.enums.RoomStatus;
import org.example.service.domain.BookingService;
import org.example.service.domain.ExportService;
import org.example.service.domain.RoomService;
import org.example.service.domain.VisitorService;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.booking.request.BookingFilterRequest;
import org.example.web.dto.booking.request.SettleRequest;
import org.example.web.dto.booking.response.BookingPageResponse;
import org.example.web.dto.booking.response.BookingResponse;
import org.example.web.dto.booking.response.BookingShortResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebBookingServiceImplTest {

    @Mock
    private BookingService bookingService;
    @Mock
    private RoomService roomService;
    @Mock
    private VisitorService visitorService;
    @Mock
    private ExportService exportService;
    @Mock
    private BookingMapper bookingMapper;
    @InjectMocks
    private WebBookingServiceImpl webBookingService;

    private static Stream<Arguments> paginationTestCases() {
        return Stream.of(
                Arguments.of(0, 5, 20L),
                Arguments.of(1, 5, 20L),
                Arguments.of(0, 100, 20L),
                Arguments.of(100, 0, 0L)
        );
    }

    @ParameterizedTest
    @MethodSource("paginationTestCases")
    void shouldGetAllBookingsAndReturnBookingPageResponseWhenValidRequestTest(int page, int size, Long total) {
        // given
        BookingFilterRequest filterRequest = new BookingFilterRequest(page, size);
        List<Booking> expectedBookings = List.of(TestObjectUtils.createTestbooking());
        List<BookingShortResponse> bookingShortResponses = List.of(TestObjectUtils.createBookingShortResponse());
        BookingPageResponse expectedResponse = new BookingPageResponse(bookingShortResponses, page, size, total);

        // when
        when(bookingService.getBookings(page, size)).thenReturn(expectedBookings);
        when(bookingService.countAll()).thenReturn(total);
        when(bookingMapper.toPageResponse(expectedBookings, page, size, total)).thenReturn(expectedResponse);
        BookingPageResponse response = webBookingService.getAllBookings(filterRequest);

        // then
        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void shouldGetBookingByIdAndReturnBookingResponseWhenBookingExistsTest() {
        // given
        Booking expectedBooking = TestObjectUtils.createTestbooking();
        BookingResponse expectedResponse = new BookingResponse();

        // when
        when(bookingService.getById(anyLong())).thenReturn(expectedBooking);
        when(bookingMapper.toResponse(expectedBooking)).thenReturn(expectedResponse);
        BookingResponse response = webBookingService.getBookingById(1L);

        // then
        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void shouldEvictVisitorFromRoomAndReturnSuccessMessageWhenVisitorEvictedTest() {
        // given
        Booking expectedBooking = TestObjectUtils.createTestbooking();

        // when
        when(bookingService.getById(anyLong())).thenReturn(expectedBooking);
        ResponseEntity<String> actualResponse = webBookingService.evictVisitorFromRoom(1L);

        // then
        assertEquals("Visitor successfully evicted from room.", actualResponse.getBody());
        verify(bookingService, times(1)).evictVisitorFromRoom(expectedBooking);
    }

    @Test
    void shouldSettleVisitorInRoomAndReturnSuccessMessageWhenVisitorSettledTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();
        Visitor expectedVisitor = TestObjectUtils.createTestVisitor();
        SettleRequest settleRequest = TestObjectUtils.createSettleRequest();

        // when
        when(roomService.getById(anyLong())).thenReturn(expectedRoom);
        when(visitorService.getById(anyLong())).thenReturn(expectedVisitor);
        ResponseEntity<String> actualResponse = webBookingService.settleVisitorInRoom(settleRequest);

        // then
        assertEquals("Booking successfully saved. Visitor settled in room.", actualResponse.getBody());
        verify(bookingService, times(1)).settleVisitorInRoom(expectedRoom, expectedVisitor, settleRequest.getDuration(), settleRequest.getSettleDate());
    }
}
