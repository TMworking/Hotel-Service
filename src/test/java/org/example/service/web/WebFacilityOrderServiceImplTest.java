package org.example.service.web;

import org.example.mappers.FacilityOrderMapper;
import org.example.model.domain.FacilityOrder;
import org.example.service.domain.ExportService;
import org.example.service.domain.FacilityOrderService;
import org.example.service.domain.FacilityService;
import org.example.service.domain.VisitorService;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.facilityorder.request.FacilityOrderFilterRequest;
import org.example.web.dto.facilityorder.request.FacilityOrderRequest;
import org.example.web.dto.facilityorder.response.FacilityOrderPageResponse;
import org.example.web.dto.facilityorder.response.FacilityOrderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebFacilityOrderServiceImplTest {

    @Mock
    private FacilityOrderService facilityOrderService;
    @Mock
    private VisitorService visitorService;
    @Mock
    private FacilityService facilityService;
    @Mock
    private ExportService exportService;
    @Mock
    private FacilityOrderMapper facilityOrderMapper;
    @InjectMocks
    private WebFacilityOrderServiceImpl webFacilityOrderService;

    private static Stream<Arguments> paginationAndSortingTestCases() {
        return Stream.of(
                Arguments.of(0, 5, 10L, "acs", "id"),
                Arguments.of(0, 5, 10L, "desc", "date"),
                Arguments.of(1, 5, 10L, "desc", "id"),
                Arguments.of(0, 10, 10L, "asc", "date"),
                Arguments.of(100, 0, 10L, "asc", "id")
        );
    }

    @ParameterizedTest
    @MethodSource("paginationAndSortingTestCases")
    void shouldGetAllFacilityOrdersAndReturnMappedResponseTest(int page, int size, Long total, String field, String direction) {
        // given
        FacilityOrderFilterRequest expectedRequest = new FacilityOrderFilterRequest(field, direction, page, size);
        List<FacilityOrder> expectedOrders = List.of(TestObjectUtils.createTestFacilityOrder());
        FacilityOrderPageResponse expectedResponse = new FacilityOrderPageResponse();

        // when
        when(facilityOrderService.getFacilityOrders(page, size, field, direction)).thenReturn(expectedOrders);
        when(facilityService.countAll()).thenReturn(total);
        when(facilityOrderMapper.toPageResponse(expectedOrders, page, size, total)).thenReturn(expectedResponse);
        FacilityOrderPageResponse actualResponse = webFacilityOrderService.getAllFacilityOrders(expectedRequest);

        // then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldGetFacilityOrderByIdAndReturnMappedResponseTest() {
        // given
        FacilityOrder expectedFacilityOrder = TestObjectUtils.createTestFacilityOrder();
        FacilityOrderResponse expectedResponse = new FacilityOrderResponse();

        // when
        when(facilityOrderService.getById(anyLong())).thenReturn(expectedFacilityOrder);
        when(facilityOrderMapper.toResponse(expectedFacilityOrder)).thenReturn(expectedResponse);
        FacilityOrderResponse actualResponse = webFacilityOrderService.getFacilityOrderById(1L);

        // then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldSaveFacilityOrderAndCallServiceAndReturnResponseTest() {
        // given
        FacilityOrder expectedFacilityOrder = TestObjectUtils.createTestFacilityOrder();
        FacilityOrderRequest expectedRequest = TestObjectUtils.createFacilityOrderRequest();

        // when
        when(facilityService.getById(anyLong())).thenReturn(expectedFacilityOrder.getFacility());
        when(visitorService.getById(anyLong())).thenReturn(expectedFacilityOrder.getVisitor());
        ResponseEntity<String> actualResponse = webFacilityOrderService.saveFacilityOrder(expectedRequest);

        // then
        assertEquals("Facility order successfully saved", actualResponse.getBody());
        verify(facilityOrderService, times(1)).saveFacilityOrder(expectedFacilityOrder.getFacility(), expectedFacilityOrder.getVisitor(), expectedRequest.getOrderDate());
    }
}
