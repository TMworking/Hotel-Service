package org.example.service.domain;

import org.example.dao.RoomDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Booking;
import org.example.model.domain.Room;
import org.example.model.domain.Visitor;
import org.example.model.enums.RoomStatus;
import org.example.model.enums.RoomType;
import org.example.service.RoomProperties;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.room.request.RoomFilterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {

    @Mock
    private RoomDao sqlRoomDao;
    @Mock
    private RoomProperties roomProperties;
    @InjectMocks
    private RoomServiceImpl roomService;


    @Test
    void shouldFetByIdAndReturnRoomWhenRoomExistsTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(sqlRoomDao.findById(1L)).thenReturn(expectedRoom);
        Room actualRoom = roomService.getById(1L);

        // then
        assertNotNull(actualRoom, "Room shouldn't be null");
        assertEquals(expectedRoom, actualRoom, "Returned room should match the expected");
        verify(sqlRoomDao).findById(1L);
    }

    @Test
    void shouldNotGetByIdAndThrowWhenRoomNotExistsTest() {
        // given

        // when
        when(sqlRoomDao.findById(999L)).thenReturn(null);

        // then
        assertThrows(EntityNotFoundException.class,
                () -> roomService.getById(999L),
                "Should throw EntityNotFoundException when room not found");
        verify(sqlRoomDao, times(1)).findById(999L);
    }

    @Test
    void shouldGetByNumberAndReturnRoomWhenFoundTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(sqlRoomDao.findByNumber(anyString())).thenReturn(Optional.of(expectedRoom));
        Room actualRoom = roomService.getByNumber("101");

        // then
        assertNotNull(actualRoom, "Room should not be null");
        assertEquals(expectedRoom, actualRoom, "Returned room should match the expected");
    }

    @Test
    void shouldNotGetByNumberAndThrowEntityNotFoundExceptionWhenRoomNotFoundTest() {
        // given

        // when
        when(sqlRoomDao.findByNumber(anyString())).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class,
                () -> roomService.getByNumber("999"),
                "Should throw EntityNotFoundException when room not found");
        verify(sqlRoomDao, times(1)).findByNumber("999");
    }

    @Test
    void shouldSaveAndReturnRoomTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(sqlRoomDao.save(any())).thenReturn(expectedRoom);
        Room actualRoom = roomService.saveRoom(expectedRoom.getRoomNumber(), expectedRoom.getPrice(), expectedRoom.getRoomType());

        // then
        assertNotNull(actualRoom, "Saved room shouldn't be null");
        assertEquals(expectedRoom, actualRoom, "Saved room should match expected");
        verify(sqlRoomDao).save(any(Room.class));
    }

    @Test
    void shouldUpdateRoomTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        roomService.updateRoom(expectedRoom);

        // then
        verify(sqlRoomDao, times(1)).update(expectedRoom);
    }

    @Test
    void shouldSaveAllRoomsTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();
        List<Room> rooms = List.of(expectedRoom);

        // when
        roomService.saveAllRooms(rooms);

        // then
        verify(sqlRoomDao, times(1)).saveAll(rooms);
    }

    @Test
    void shouldDeleteAllRoomsTest() {
        // given
        // when
        roomService.deleteAllRooms();

        // then
        verify(sqlRoomDao, times(1)).deleteAll();
    }

    @Test
    void shouldGetRoomsAllRoomsAndReturnTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(sqlRoomDao.findAll()).thenReturn(List.of(expectedRoom));
        List<Room> actualRoom = roomService.getRooms();

        // then
        assertNotNull(actualRoom, "Room list shouldn't be null");
        assertEquals(expectedRoom, actualRoom.get(0), "Expected 1 room");
        verify(sqlRoomDao, times(1)).findAll();
    }

    @Test
    void shouldGetRoomsAllRoomsAndReturnEmptyListWhenNoRoomsTest() {
        // given

        // when
        when(sqlRoomDao.findAll()).thenReturn(Collections.emptyList());
        List<Room> actualRooms = roomService.getRooms();

        // then
        assertNotNull(actualRooms, "Room list shouldn't be null");
        assertTrue(actualRooms.isEmpty(), "Rooms list should be empty");
    }

    private static Stream<Arguments> roomFilterTestCases() {
        return Stream.of(
                Arguments.of(
                        RoomFilterRequest.builder().build()
                ),
                Arguments.of(
                        RoomFilterRequest.builder()
                                .roomType(RoomType.BASIC)
                                .build()
                ),
                Arguments.of(
                        RoomFilterRequest.builder()
                                .minPrice(new BigDecimal("100"))
                                .maxPrice(new BigDecimal("200"))
                                .build()
                ),
                Arguments.of(
                        RoomFilterRequest.builder()
                                .status(RoomStatus.FREE)
                                .build()
                ),
                Arguments.of(
                        RoomFilterRequest.builder()
                                .roomType(RoomType.DELUXE)
                                .status(RoomStatus.FREE)
                                .minPrice(new BigDecimal("50"))
                                .build()
                ),
                Arguments.of(
                        RoomFilterRequest.builder()
                                .page(0)
                                .size(10)
                                .sortBy("price")
                                .sortDirection("asc")
                                .build()
                ),
                Arguments.of(
                        RoomFilterRequest.builder()
                                .page(5)
                                .size(5)
                                .sortBy("id")
                                .sortDirection("desc")
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("roomFilterTestCases")
    void shouldGetRoomsWithFilterAndReturnFilteredListTest(RoomFilterRequest filter) {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(sqlRoomDao.findAll(filter)).thenReturn(List.of(expectedRoom));
        List<Room> actualRooms = roomService.getRooms(filter);

        // then
        assertNotNull(actualRooms, "Room list shouldn't be null");
        assertEquals(expectedRoom, actualRooms.get(0), "Expected 1 room");
        verify(sqlRoomDao, times(1)).findAll(filter);
    }

    @Test
    void shouldCountAllAndReturnCorrectCountTest() {
        // given
        Long expectedCount = 5L;

        // when
        when(sqlRoomDao.countAll()).thenReturn(expectedCount);
        Long actualCount = roomService.countAll();

        // then
        assertEquals(expectedCount, actualCount, "Expected room actualCount to be 5");
        verify(sqlRoomDao, times(1)).countAll();
    }

    @Test
    void shouldChangeRoomStatusWhenEnabledTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(roomProperties.getStatusChangeEnabled()).thenReturn(true);
        when(sqlRoomDao.findById(1L)).thenReturn(expectedRoom);
        roomService.changeRoomStatus(1L, RoomStatus.OCCUPIED);

        // then
        assertEquals(RoomStatus.OCCUPIED, expectedRoom.getStatus(), "Room status should be updated");
        verify(sqlRoomDao, times(1)).update(expectedRoom);
    }

    @Test
    void shouldNotChangeRoomStatusAndThrowWhenDisabledTest() {
        // given

        // when
        when(roomProperties.getStatusChangeEnabled()).thenReturn(false);

        // then
        assertThrows(IllegalStateException.class,
                () -> roomService.changeRoomStatus(1L, RoomStatus.OCCUPIED),
                "Should throw IllegalStateException when status change is disabled");
    }

    @Test
    void shouldGetLastRoomVisitorsAndReturnLimitedListTest() {
        // given
        Booking expectedBooking = TestObjectUtils.createTestbooking();
        Room expectedRoom = TestObjectUtils.createTestRoom();
        List<Booking> expectedBookings = new ArrayList<>(List.of(expectedBooking, expectedBooking, expectedBooking));

        // when
        when(roomProperties.getHistorySize()).thenReturn(2);
        when(sqlRoomDao.findRoomBookings(anyLong())).thenReturn(expectedBookings);
        List<Visitor> actualVisitors = roomService.getLastRoomVisitors(expectedRoom);

        // then
        assertNotNull(actualVisitors, "Visitor list shouldn't be null");
        assertEquals(2, actualVisitors.size(), "Expected 2 visitors");
    }

    @Test
    void ShouldGetFreeRoomsAndReturnFreeRoomsTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(sqlRoomDao.findFreeRooms()).thenReturn(List.of(expectedRoom));
        List<Room> actualRooms = roomService.getFreeRooms();

        // then
        assertNotNull(actualRooms, "Room list shouldn't be null");
        assertEquals(expectedRoom, actualRooms.get(0), "Expected 1 free room");
    }

    @Test
    void shouldGetFreeRoomsAndReturnEmptyListWhenNoFreeRoomsTest() {
        // given

        // when
        when(sqlRoomDao.findFreeRooms()).thenReturn(Collections.emptyList());
        List<Room> actualRooms = roomService.getFreeRooms();

        // then
        assertNotNull(actualRooms, "Room list shouldn't be null");
        assertTrue(actualRooms.isEmpty(), "Rooms list should be empty");
    }

    @Test
    void shouldGetRoomFreeDateAndReturnLatestEvictDateTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();
        Booking expectedBooking = TestObjectUtils.createTestbooking();

        // when
        when(sqlRoomDao.findRoomBookings(anyLong())).thenReturn(List.of(expectedBooking));
        LocalDateTime actualDate = roomService.getRoomFreeDate(expectedRoom);

        // then
        assertEquals(expectedBooking.getSettleDate().plusDays(expectedBooking.getDuration()), actualDate, "Room free date should match expected");
    }

    @Test
    void shouldGetFreeRoomsByTypeReturnRoomsTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(sqlRoomDao.findFreeRoomsByType(RoomType.BASIC)).thenReturn(List.of(expectedRoom));
        List<Room> actualRooms = roomService.getFreeRoomsByType(RoomType.BASIC);

        // then
        assertNotNull(actualRooms, "Room list shouldn't be null");
        assertEquals(1, actualRooms.size(), "Expected 1 free room of type SINGLE");
    }

    @Test
    void shouldGetRoomsSortedByPriceAndReturnListTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(sqlRoomDao.findRoomsSortedByPrice()).thenReturn(List.of(expectedRoom));
        List<Room> actualRooms = roomService.getRoomsSortedByPrice();

        // then
        assertNotNull(actualRooms, "Room list shouldn't be null");
        assertEquals(expectedRoom, actualRooms.get(0), "Expected 1 room sorted by price");
    }

    @Test
    void shouldGetRoomsSortedByPriceAndReturnEmptyListWhenNoRoomsTest() {
        // given

        // when
        when(sqlRoomDao.findRoomsSortedByPrice()).thenReturn(Collections.emptyList());
        List<Room> actualRooms = roomService.getRoomsSortedByPrice();

        // then
        assertNotNull(actualRooms, "Room list shouldn't be null");
        assertTrue(actualRooms.isEmpty(), "Rooms list should be empty");
    }

    @Test
    void shouldGetRoomsSortedByTypeAndReturnRoomsListTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(sqlRoomDao.findRoomsSortedByType()).thenReturn(List.of(expectedRoom));
        List<Room> actualRooms = roomService.getRoomsSortedByType();

        // then
        assertNotNull(actualRooms, "Room list shouldn't be null");
        assertEquals(expectedRoom, actualRooms.get(0), "Expected 1 room sorted by type");
    }

    @Test
    void shouldGetRoomsSortedByTypeAndReturnEmptyListWhenNoRoomsTest() {
        // given

        // when
        when(sqlRoomDao.findRoomsSortedByType()).thenReturn(Collections.emptyList());
        List<Room> actualRooms = roomService.getRoomsSortedByType();

        // then
        assertNotNull(actualRooms, "Room list shouldn't be null");
        assertTrue(actualRooms.isEmpty(), "Rooms list should be empty");
    }

    @Test
    void shouldGetRoomsBookingsAndReturnBookingsListTest() {
        // given
        Booking expectedBooking = TestObjectUtils.createTestbooking();
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(sqlRoomDao.findRoomBookings(anyLong())).thenReturn(List.of(expectedBooking));
        List<Booking> actualBookings = roomService.getRoomsBookings(expectedRoom);

        // then
        assertNotNull(actualBookings, "Booking list shouldn't be null");
        assertEquals(expectedBooking, actualBookings.get(0), "Expected 1 booking");
    }

    @Test
    void shouldGetRoomsBookingsAndReturnEmptyListWhenNoBookingsTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();

        // when
        when(sqlRoomDao.findRoomBookings(1L)).thenReturn(Collections.emptyList());
        List<Booking> actualBookings = roomService.getRoomsBookings(expectedRoom);

        // then
        assertNotNull(actualBookings, "Booking list shouldn't be null");
        assertTrue(actualBookings.isEmpty(), "Booking list should be empty");
    }
}
