package org.example.service.web;

import org.example.mappers.FacilityMapper;
import org.example.model.domain.Facility;
import org.example.service.domain.ExportService;
import org.example.service.domain.FacilityService;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.facility.request.FacilityFilterRequest;
import org.example.web.dto.facility.request.FacilityRequest;
import org.example.web.dto.facility.response.FacilityPageResponse;
import org.example.web.dto.facility.response.FacilityResponse;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebFacilityServiceImplTest {

    @Mock
    private FacilityService facilityService;
    @Mock
    private ExportService exportService;
    @Mock
    private FacilityMapper facilityMapper;
    @InjectMocks
    private WebFacilityServiceImpl webFacilityService;

    @Test
    void shouldSaveFacilityAndCallServiceAndReturnSuccessMessageTest() {
        // given
        FacilityRequest expectedRequest = TestObjectUtils.createFacilityRequest();

        // when
        ResponseEntity<String> actualResponse = webFacilityService.saveFacility(expectedRequest);

        // then
        assertEquals("Facility successfully saved", actualResponse.getBody());
        verify(facilityService, times(1)).saveFacility(expectedRequest.getCost(), expectedRequest.getFacilityType());
    }

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
    void shouldGetAllFacilitiesAndReturnPagedResponseTest(int page, int size, Long total) {
        // given
        FacilityFilterRequest expectedRequest = new FacilityFilterRequest(page, size);
        List<Facility> expectedFacilities = List.of(TestObjectUtils.createTestFacility());
        FacilityPageResponse pageResponse = new FacilityPageResponse();

        // when
        when(facilityService.getFacilities(page, size)).thenReturn(expectedFacilities);
        when(facilityService.countAll()).thenReturn(total);
        when(facilityMapper.toPageResponse(expectedFacilities, page, size, total)).thenReturn(pageResponse);
        FacilityPageResponse actualResponse = webFacilityService.getAllFacilities(expectedRequest);

        // then
        assertEquals(pageResponse, actualResponse);
    }

    @Test
    void shouldGetFacilityByIdAndReturnMappedResponseTest() {
        // given
        Facility expectedFacility = TestObjectUtils.createTestFacility();
        FacilityResponse expectedResponse = new FacilityResponse();

        // when
        when(facilityService.getById(anyLong())).thenReturn(expectedFacility);
        when(facilityMapper.toResponse(expectedFacility)).thenReturn(expectedResponse);
        FacilityResponse actualResponse = webFacilityService.getFacilityById(1L);

        // then
        assertEquals(expectedResponse, actualResponse);
    }
}
