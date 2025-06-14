package org.example.service.web;

import org.example.mappers.RoomMapper;
import org.example.mappers.VisitorMapper;
import org.example.model.domain.Room;
import org.example.model.enums.RoomStatus;
import org.example.model.enums.RoomType;
import org.example.service.domain.ExportService;
import org.example.service.domain.RoomService;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.room.request.RoomFilterRequest;
import org.example.web.dto.room.request.RoomRequest;
import org.example.web.dto.room.response.RoomPageResponse;
import org.example.web.dto.room.response.RoomResponse;
import org.example.web.dto.visitor.response.VisitorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebRoomServiceImplTest {

    @Mock
    private RoomService roomService;
    @Mock
    private ExportService exportService;
    @Mock
    private RoomMapper roomMapper;
    @Mock
    private VisitorMapper visitorMapper;
    @InjectMocks
    private WebRoomServiceImpl webRoomService;

    @Test
    void shouldGetRoomsByIdAndReturnMappedRoomResponseTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();
        RoomResponse expectedResponse = new RoomResponse();

        // when
        when(roomService.getById(anyLong())).thenReturn(expectedRoom);
        when(roomMapper.toResponse(expectedRoom)).thenReturn(expectedResponse);
        RoomResponse actualResponse = webRoomService.getRoomsById(1L);

        // then
        assertEquals(expectedResponse, actualResponse);
        verify(roomService, times(1)).getById(1L);
        verify(roomMapper, times(1)).toResponse(expectedRoom);
    }

    private static Stream<Arguments> roomFilterTestCases() {
        return Stream.of(
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
    void shouldGetAllRoomsAndReturnPagedResponseTest(RoomFilterRequest filter) {
        // given
        List<Room> expectedRooms = List.of(TestObjectUtils.createTestRoom());
        RoomPageResponse expectedResponse = new RoomPageResponse();

        // when
        when(roomService.getRooms(filter)).thenReturn(expectedRooms);
        when(roomService.countAll()).thenReturn(1L);
        when(roomMapper.toPageResponse(expectedRooms, filter.getPage(), filter.getSize(), 1L)).thenReturn(expectedResponse);
        RoomPageResponse actualResponse = webRoomService.getAllRooms(filter);

        // then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldChangeRoomStatusAndReturnOkResponseTest() {
        // given
        // when
        ResponseEntity<String> actualResponse = webRoomService.changeRoomStatus(1L, RoomStatus.OCCUPIED);

        // then
        assertEquals("Room status successfully changed", actualResponse.getBody());
        verify(roomService).changeRoomStatus(1L, RoomStatus.OCCUPIED);
    }

    @Test
    void shouldGetRoomLastVisitorsAndReturnVisitorResponsesTest() {
        // given
        Room expectedRoom = TestObjectUtils.createTestRoom();
        List<VisitorResponse> expectedPage = List.of(new VisitorResponse());

        // when
        when(roomService.getById(anyLong())).thenReturn(expectedRoom);
        when(roomService.getLastRoomVisitors(expectedRoom)).thenReturn(List.of());
        when(visitorMapper.toResponseList(List.of())).thenReturn(expectedPage);
        List<VisitorResponse> actualResponse = webRoomService.getRoomLastVisitors(1L);

        // then
        assertEquals(expectedPage, actualResponse);
    }

    @Test
    void shouldSaveRoomAndReturnSuccessMessageTest() {
        // given
        RoomRequest expectedRequest = TestObjectUtils.createRoomRequest();

        // when
        ResponseEntity<String> actualResponse = webRoomService.saveRoom(expectedRequest);

        assertEquals("Room successfully saved", actualResponse.getBody());
        verify(roomService).saveRoom(expectedRequest.getRoomNumber(), expectedRequest.getCost(), expectedRequest.getRoomType());
    }
}
