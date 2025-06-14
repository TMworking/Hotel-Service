package org.example.service.domain;

import org.example.dao.VisitorDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Booking;
import org.example.model.domain.Facility;
import org.example.model.domain.FacilityOrder;
import org.example.model.domain.Visitor;
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
import java.util.stream.Stream;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VisitorServiceImplTest {

    @Mock
    private VisitorDao sqlVisitorDao;

    @InjectMocks
    private VisitorServiceImpl visitorService;

    @Test
    void shouldGetByIdAndReturnVisitorWhenVisitorExistsTest() {
        // given
        Visitor expectedVisitor = TestObjectUtils.createTestVisitor();

        // when
        when(sqlVisitorDao.findById(1L)).thenReturn(expectedVisitor);
        Visitor result = visitorService.getById(1L);

        // then
        assertNotNull(result, "Visitor shouldn't be null");
        assertEquals(expectedVisitor, result, "Returned visitor should match expected");
        verify(sqlVisitorDao, times(1)).findById(1L);
    }

    @Test
    void shouldNotGetByIdAndThrowExceptionWhenVisitorNotFoundTest() {
        // given

        // when
        when(sqlVisitorDao.findById(999L)).thenReturn(null);

        // then
        assertThrows(EntityNotFoundException.class,
                () -> visitorService.getById(999L),
                "Should throw EntityNotFoundException when visitor not found");
        verify(sqlVisitorDao, times(1)).findById(999L);
    }

    @Test
    void shouldSaveAndReturnVisitorTest() {
        // given
        Visitor expectedVisitor = TestObjectUtils.createTestVisitor();

        // when
        when(sqlVisitorDao.save(any(Visitor.class))).thenReturn(expectedVisitor);
        Visitor actualVisitor = visitorService.saveVisitor(
                expectedVisitor.getName(),
                expectedVisitor.getSurname(),
                expectedVisitor.getPassport());

        // then
        assertNotNull(actualVisitor, "Saved visitor shouldn't be null");
        assertEquals(expectedVisitor, actualVisitor, "Saved visitor should match expected");
        verify(sqlVisitorDao, times(1)).save(any(Visitor.class));
    }

    @Test
    void shouldSaveAllVisitorsTest() {
        // given
        Visitor expectedVisitor = TestObjectUtils.createTestVisitor();
        List<Visitor> expectedVisitors = List.of(expectedVisitor);

        // when
        visitorService.saveAllVisitors(expectedVisitors);

        // then
        verify(sqlVisitorDao, times(1)).saveAll(expectedVisitors);
    }

    @Test
    void shouldDeleteAllVisitorsTest() {
        // given
        visitorService.deleteAllVisitors();

        // when
        // then
        verify(sqlVisitorDao, times(1)).deleteAll();
    }

    @Test
    void shouldGetVisitorsSortedByNameAndReturnSortedVisitorsTest() {
        // given
        Visitor expectedVisitor = TestObjectUtils.createTestVisitor();

        // when
        when(sqlVisitorDao.findVisitorsSortedByName()).thenReturn(List.of(expectedVisitor));
        List<Visitor> actualVisitors = visitorService.getVisitorsSortedByName();

        // then
        assertNotNull(actualVisitors, "Visitor list shouldn't be null");
        assertEquals(expectedVisitor, actualVisitors.get(0), "Expected 1 visitor");
        verify(sqlVisitorDao, times(1)).findVisitorsSortedByName();
    }

    @Test
    void shouldGetVisitorFacilitiesAndReturnFacilitiesFromOrdersTest() {
        // given
        Visitor expectedVisitor = TestObjectUtils.createTestVisitor();
        FacilityOrder expectedFacilityOrder = TestObjectUtils.createTestFacilityOrder();
        expectedVisitor.setFacilityOrderList(List.of(expectedFacilityOrder));

        // when
        List<Facility> actualFacilities = visitorService.getVisitorFacilities(expectedVisitor);

        // then
        assertNotNull(actualFacilities, "Facility list shouldn't be null");
        assertEquals(expectedFacilityOrder.getFacility(), actualFacilities.get(0), "Facility should match the expected one");
    }

    @Test
    void shouldGetVisitorFacilitiesAndReturnEmptyListWhenNoOrdersTest() {
        // given
        Visitor expectedVisitor = TestObjectUtils.createTestVisitor();
        expectedVisitor.setFacilityOrderList(Collections.emptyList());

        // when
        List<Facility> actualFacilities = visitorService.getVisitorFacilities(expectedVisitor);

        // then
        assertNotNull(actualFacilities, "Facility list shouldn't be null");
        assertTrue(actualFacilities.isEmpty(), "Facility list should be empty");
    }

    @Test
    void shouldGetVisitorsBookingsAndReturnBookingsListTest() {
        // given
        Visitor expectedVisitor = TestObjectUtils.createTestVisitor();
        List<Booking> bookings = List.of(TestObjectUtils.createTestbooking());

        // when
        when(sqlVisitorDao.findVisitorsBookings(any(Visitor.class))).thenReturn(bookings);
        List<Booking> actualBookings = visitorService.getVisitorsBookings(expectedVisitor);

        // then
        assertNotNull(actualBookings, "Booking list shouldn't be null");
        assertEquals(1, actualBookings.size(), "Expected 1 booking");
        verify(sqlVisitorDao, times(1)).findVisitorsBookings(expectedVisitor);
    }

    @Test
    void shouldGetVisitorsAndReturnAllVisitorsTest() {
        // given
        Visitor expectedVisitor = TestObjectUtils.createTestVisitor();

        // when
        when(sqlVisitorDao.findAll()).thenReturn(List.of(expectedVisitor));
        List<Visitor> actualVisitors = visitorService.getVisitors();

        // then
        assertNotNull(actualVisitors, "Visitor list shouldn't be null");
        assertEquals(expectedVisitor, actualVisitors.get(0), "Expected 1 visitor");
        verify(sqlVisitorDao, times(1)).findAll();
    }

    @Test
    void shouldGetVisitorsAndReturnEmptyListWhenNoVisitorsExistTest() {
        // given

        // when
        when(sqlVisitorDao.findAll()).thenReturn(Collections.emptyList());
        List<Visitor> actualVisitors = visitorService.getVisitors();

        // then
        assertNotNull(actualVisitors, "Visitor list shouldn't be null");
        assertTrue(actualVisitors.isEmpty(), "Visitor list should be empty");
        verify(sqlVisitorDao, times(1)).findAll();
    }

    private static Stream<Arguments> paginationAndSortingTestCases() {
        return Stream.of(
                Arguments.of(0, 5, "acs", "id"),
                Arguments.of(0, 5, "desc", "name"),
                Arguments.of(1, 5, "desc", "id"),
                Arguments.of(0, 10, "asc", "surname"),
                Arguments.of(100, 0, "asc", "id")
        );
    }

    @ParameterizedTest
    @MethodSource("paginationAndSortingTestCases")
    void shouldGetVisitorsWithSortingAndReturnPagedListVisitorsTest(int page, int size, String field, String direction) {
        // given
        Visitor expectedVisitor = TestObjectUtils.createTestVisitor();

        // when
        when(sqlVisitorDao.findAll(page, size, field, direction)).thenReturn(List.of(expectedVisitor));
        List<Visitor> actualVisitors = visitorService.getVisitors(page, size, field, direction);

        // then
        assertNotNull(actualVisitors, "Visitor list shouldn't be null");
        assertEquals(expectedVisitor, actualVisitors.get(0), "Expected 1 visitor");
        verify(sqlVisitorDao, times(1)).findAll(page, size, field, direction);
    }

    @ParameterizedTest
    @MethodSource("paginationAndSortingTestCases")
    void shouldGetVisitorsWithSortingAndReturnEmptyListWhenNoVisitorsExistTest(int page, int size, String field, String direction) {
        // given

        // when
        when(sqlVisitorDao.findAll(page, size, field, direction)).thenReturn(Collections.emptyList());
        List<Visitor> actualVisitors = visitorService.getVisitors(page, size, field, direction);

        // then
        assertNotNull(actualVisitors, "Visitor list shouldn't be null");
        assertTrue(actualVisitors.isEmpty(), "Visitor list should be empty");
        verify(sqlVisitorDao, times(1)).findAll(page, size, field, direction);
    }

    @Test
    void shouldCountAllAndReturnCorrectCountTest() {
        // given
        Long expectedCount = 5L;

        // when
        when(sqlVisitorDao.countAll()).thenReturn(expectedCount);
        Long count = visitorService.countAll();

        // then
        assertEquals(expectedCount, count, "Expected visitor count to be 5");
        verify(sqlVisitorDao, times(1)).countAll();
    }
}
