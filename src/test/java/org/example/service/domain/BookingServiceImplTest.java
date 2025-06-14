package org.example.service.domain;

import org.example.dao.BookingDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Booking;
import org.example.model.domain.Room;
import org.example.model.enums.RoomStatus;
import org.example.utils.TestObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    private RoomService roomService;

    @Mock
    private BookingDao sqlBookingDao;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void ShouldFindByIdAndReturnWhenBookingExistsTest() {
        // given
        Booking expectedBooking = TestObjectUtils.createTestbooking();

        // when
        when(sqlBookingDao.findById(1L)).thenReturn(Optional.of(expectedBooking));
        Booking actualBooking  = bookingService.getById(1L);

        // then
        assertNotNull(actualBooking, "Booking shouldn't be null");
        assertEquals(expectedBooking, actualBooking, "Return value should be equal expected one");
        verify(sqlBookingDao, times(1)).findById(1L);
    }

    @Test
    void ShouldNotFindByIdAndThrowExceptionWhenBookingNotExistsTest() {
        // given

        // when
        when(sqlBookingDao.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class,
                () -> bookingService.getById(999L),
                "Should be thrown Entity not found exception");
        verify(sqlBookingDao, times(1)).findById(999L);
    }

    @Test
    void ShouldSaveNewBookingAndReturnSavedBookingTest() {
        // given
        Booking expectedBooking = TestObjectUtils.createTestbooking();

        // when
        when(sqlBookingDao.save(any(Booking.class))).thenReturn(expectedBooking);
        Booking actualBooking = bookingService.saveBooking(
                expectedBooking.getRoom(),
                expectedBooking.getVisitor(),
                expectedBooking.getSettleDate(),
                expectedBooking.getDuration()
        );

        // then
        assertNotNull(actualBooking, "Saved booking shouldn't be null");
        assertEquals(expectedBooking, actualBooking, "Return value should match expected one");
        verify(sqlBookingDao, times(1)).save(any(Booking.class));
    }

    @Test
    void ShouldSaveAllBookingsTest() {
        // given
        List<Booking> bookings = Collections.singletonList(TestObjectUtils.createTestbooking());

        // when
        bookingService.saveAllBookings(bookings);

        // then
        verify(sqlBookingDao, times(1)).saveAll(bookings);
    }

    @Test
    void ShouldDeleteAllBookingsTest() {
        // given

        // when
        bookingService.deleteAllBookings();

        // then
        verify(sqlBookingDao, times(1)).deleteAll();
    }

    @Test
    void ShouldSettleVisitorInFreeRoomSuccessfullyTest() {
        // given
        Booking expectedBooking = TestObjectUtils.createTestbooking();

        // when
        when(sqlBookingDao.save(any(Booking.class))).thenReturn(expectedBooking);
        bookingService.settleVisitorInRoom(
                expectedBooking.getRoom(),
                expectedBooking.getVisitor(),
                expectedBooking.getDuration(),
                expectedBooking.getSettleDate()
        );

        // then
        assertEquals(RoomStatus.OCCUPIED, expectedBooking.getRoom().getStatus(), "RoomStatus should be OCCUPIED");
        verify(roomService, times(1)).updateRoom(expectedBooking.getRoom());
        verify(sqlBookingDao, times(1)).save(any(Booking.class));
    }

    @Test
    void ShouldNotSettleVisitorInOccupiedRoomTest() {
        // given
        Booking expectedBooking = TestObjectUtils.createTestbooking();
        Room testRoom = TestObjectUtils.createTestRoom();
        testRoom.setStatus(RoomStatus.OCCUPIED);
        expectedBooking.setRoom(testRoom);

        // when
        assertThrows(IllegalStateException.class,
                () -> bookingService.settleVisitorInRoom(
                        expectedBooking.getRoom(),
                        expectedBooking.getVisitor(),
                        expectedBooking.getDuration(),
                        expectedBooking.getSettleDate()
                ));

        // then
        verify(roomService, never()).updateRoom(any());
        verify(sqlBookingDao, never()).save(any());
    }

    @Test
    void shouldEvictVisitorFromRoomAndSetRoomStatusToFreeTest() {
        // given
        Booking expectedBooking = TestObjectUtils.createTestbooking();
        Room testRoom = TestObjectUtils.createTestRoom();
        testRoom.setStatus(RoomStatus.OCCUPIED);
        expectedBooking.setRoom(testRoom);

        // when
        bookingService.evictVisitorFromRoom(expectedBooking);

        // then
        assertEquals(RoomStatus.FREE, expectedBooking.getRoom().getStatus(), "RoomStatus should be FREE");
        verify(roomService, times(1)).updateRoom(expectedBooking.getRoom());
    }

    @Test
    void ShouldNotEvictVisitorFromNotExistingBookingTest() {
        // given
        Booking expectedBooking = TestObjectUtils.createTestbooking();
        expectedBooking.setRoom(null);

        // when
        // then
        assertThrows(NullPointerException.class, () -> {
            bookingService.evictVisitorFromRoom(expectedBooking);
        });
        verifyNoInteractions(roomService);
    }

    @Test
    void ShouldGetAndReturnBookingsListWhenBookingsExistTest() {
        // given
        List<Booking> expectedBookings = Collections.singletonList(TestObjectUtils.createTestbooking());

        // when
        when(sqlBookingDao.findAll()).thenReturn(expectedBookings);
        List<Booking> actualBookings = bookingService.getBookings();

        // then
        assertNotNull(actualBookings, "Result shouldn't be null");
        assertEquals(expectedBookings, actualBookings, "Should contain one booking");
        verify(sqlBookingDao, times(1)).findAll();
    }

    @Test
    void ShouldGetBookingsAndReturnEmptyListWhenNoBookingsExistTest() {
        // given

        // when
        when(sqlBookingDao.findAll()).thenReturn(Collections.emptyList());
        List<Booking> result = bookingService.getBookings();

        // then
        assertNotNull(result, "Result shouldn't be null");
        assertTrue(result.isEmpty(), "List should be empty");
        verify(sqlBookingDao, times(1)).findAll();
    }


    private static Stream<Arguments> paginationTestCases() {
        return Stream.of(
                Arguments.of(0, 5),
                Arguments.of(1, 5),
                Arguments.of(0, 10),
                Arguments.of(100, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("paginationTestCases")
    void shouldGetBookingsWithPaginationAndReturnListWhenBookingsExistTest(int page, int size) {
        // given
        List<Booking> expectedBookings  = Collections.singletonList(TestObjectUtils.createTestbooking());

        // when
        when(sqlBookingDao.findAll(page, size)).thenReturn(expectedBookings);
        List<Booking> actualBookings = bookingService.getBookings(page, size);

        // then
        assertNotNull(actualBookings);
        assertEquals(expectedBookings, actualBookings);
        verify(sqlBookingDao, times(1)).findAll(page, size);
    }

    @ParameterizedTest
    @MethodSource("paginationTestCases")
    void shouldGetBookingsWithPaginationAndReturnEmptyListWhenNoBookingsExistTest(int page, int size) {
        // given

        // when
        when(sqlBookingDao.findAll(page, size)).thenReturn(Collections.emptyList());
        List<Booking> actualBookings = bookingService.getBookings(page, size);

        // then
        assertNotNull(actualBookings, "Result should not be null");
        assertTrue(actualBookings.isEmpty(), "Result should be empty");
        verify(sqlBookingDao, times(1)).findAll(page, size);
    }

    @Test
    void shouldGetBookingsCountAndReturnCorrectCountWhenBookingsExistTest() {
        // given

        // when
        when(sqlBookingDao.countAll()).thenReturn(5L);
        Long actualResult = bookingService.countAll();

        // then
        assertNotNull(actualResult, "Result should not be null");
        assertEquals(5L, actualResult, "Should return correct count when bookings exist");
        verify(sqlBookingDao, times(1)).countAll();
    }

    @Test
    void shouldGetBookingsCountAndReturnZeroWhenNoBookingsExistTest() {
        // given

        // when
        when(sqlBookingDao.countAll()).thenReturn(0L);
        Long actualResult = bookingService.countAll();

        // then
        assertNotNull(actualResult, "Result should not be null");
        assertEquals(0L, actualResult, "Should return 0 when no bookings exist");
        verify(sqlBookingDao, times(1)).countAll();
    }
}
