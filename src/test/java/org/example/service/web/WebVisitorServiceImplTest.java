package org.example.service.web;

import org.example.mappers.VisitorMapper;
import org.example.model.domain.Visitor;
import org.example.service.domain.ExportService;
import org.example.service.domain.VisitorService;
import org.example.utils.TestObjectUtils;
import org.example.web.dto.visitor.request.VisitorFilterRequest;
import org.example.web.dto.visitor.request.VisitorRequest;
import org.example.web.dto.visitor.response.VisitorPageResponse;
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

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WebVisitorServiceImplTest {

    @Mock
    private VisitorService visitorService;
    @Mock
    private VisitorMapper visitorMapper;
    @Mock
    private ExportService exportService;
    @InjectMocks
    private WebVisitorServiceImpl webVisitorService;

    static Stream<Arguments> paginationAndSortingTestCases() {
        return Stream.of(
                Arguments.of(0, 5, "acs", "id"),
                Arguments.of(0, 5, "desc", "name"),
                Arguments.of(1, 5, "desc", "id"),
                Arguments.of(0, 10, "asc", "surname"),
                Arguments.of(100, 0, "asc", "name")
        );
    }

    @ParameterizedTest
    @MethodSource("paginationAndSortingTestCases")
    void shouldGetAllVisitorsAndReturnMappedPageTest(int page, int size, String direction, String field) {
        // given
        VisitorFilterRequest expectedRequest = new VisitorFilterRequest(field, direction, page, size);
        List<Visitor> expectedVisitors = List.of(TestObjectUtils.createTestVisitor());
        VisitorPageResponse expectedResponse = new VisitorPageResponse();

        // when
        when(visitorService.getVisitors(page, size, field, direction)).thenReturn(expectedVisitors);
        when(visitorService.countAll()).thenReturn(1L);
        when(visitorMapper.toPageResponse(expectedVisitors, page, size, 1L)).thenReturn(expectedResponse);
        VisitorPageResponse actualResponse = webVisitorService.getAllVisitors(expectedRequest);

        // then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldGetVisitorByIdAndReturnMappedVisitorTest() {
        // given
        Visitor expectedVisitor = TestObjectUtils.createTestVisitor();
        VisitorResponse expectedResponse = new VisitorResponse();

        // when
        when(visitorService.getById(anyLong())).thenReturn(expectedVisitor);
        when(visitorMapper.toResponse(expectedVisitor)).thenReturn(expectedResponse);
        VisitorResponse actualResponse = webVisitorService.getVisitorById(1L);

        // then
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldSaveVisitorAndCallServiceAndReturnSuccessMessageTest() {
        // given
        VisitorRequest expectedRequest = TestObjectUtils.createVisitorRequest();

        // when
        ResponseEntity<String> actualResult = webVisitorService.saveVisitor(expectedRequest);

        // then
        assertEquals("Visitor successfully saved", actualResult.getBody());
        verify(visitorService).saveVisitor(expectedRequest.getName(), expectedRequest.getSurname(), expectedRequest.getPassport());
    }
}
