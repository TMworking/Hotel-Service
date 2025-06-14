package org.example.service.domain;

import org.example.dao.FacilityOrderDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.FacilityOrder;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FacilityOrderServiceImplTest {

    @Mock
    private FacilityOrderDao sqlFacilityOrderDao;

    @InjectMocks
    private FacilityOrderServiceImpl facilityOrderService;

    @Test
    void shouldGetByIdReturnFacilityOrderWhenFacilityOrderExistsTest() {
        // given
        FacilityOrder expectedFacilityOrder = TestObjectUtils.createTestFacilityOrder();

        // when
        when(sqlFacilityOrderDao.findById(1L)).thenReturn(Optional.of(expectedFacilityOrder));
        FacilityOrder actualFacilityOrder = facilityOrderService.getById(1L);

        // then
        assertNotNull(actualFacilityOrder, "FacilityOrder shouldn't be null");
        assertEquals(expectedFacilityOrder, actualFacilityOrder, "Returned facility order should match the expected one");
        verify(sqlFacilityOrderDao, times(1)).findById(1L);
    }

    @Test
    void shouldNotGetByIdAndThrowExceptionWhenFacilityOrderNotFoundTest() {
        // given

        // when
        when(sqlFacilityOrderDao.findById(anyLong())).thenReturn(Optional.empty());

        // then
        assertThrows(EntityNotFoundException.class,
                () -> facilityOrderService.getById(999L),
                "Should throw EntityNotFoundException when FacilityOrder not found");
        verify(sqlFacilityOrderDao, times(1)).findById(999L);
    }

    @Test
    void shouldSaveFacilityOrderAndReturnTest() {
        // given
        FacilityOrder expectedFacilityOrder = TestObjectUtils.createTestFacilityOrder();

        // when
        when(sqlFacilityOrderDao.save(any(FacilityOrder.class))).thenReturn(expectedFacilityOrder);
        FacilityOrder actualFacilityOrder = facilityOrderService.saveFacilityOrder(
                expectedFacilityOrder.getFacility(),
                expectedFacilityOrder.getVisitor(),
                expectedFacilityOrder.getDate()
        );

        // then
        assertNotNull(actualFacilityOrder, "Saved facility order shouldn't be null");
        verify(sqlFacilityOrderDao, times(1)).save(any(FacilityOrder.class));
    }

    @Test
    void shouldSaveAllFacilityOrdersTest() {
        // given
        FacilityOrder expectedFacilityOrder = TestObjectUtils.createTestFacilityOrder();
        List<FacilityOrder> expectedOrders = Collections.singletonList(expectedFacilityOrder);

        // when
        facilityOrderService.saveAllFacilityOrders(expectedOrders);

        // then
        verify(sqlFacilityOrderDao, times(1)).saveAll(expectedOrders);
    }

    @Test
    void shouldDeleteAllFacilityOrdersTest() {
        // given
        // when
        facilityOrderService.deleteAllFacilityOrders();

        // then
        verify(sqlFacilityOrderDao, times(1)).deleteAll();
    }

    @Test
    void shouldGetFacilityOrdersReturnFacilityOrdersTest() {
        // given
        FacilityOrder expectedFacilityOrder = TestObjectUtils.createTestFacilityOrder();
        List<FacilityOrder> expectedOrders = Collections.singletonList(expectedFacilityOrder);

        // when
        when(sqlFacilityOrderDao.findAll()).thenReturn(expectedOrders);
        List<FacilityOrder> actualList = facilityOrderService.getFacilityOrders();

        // then
        assertNotNull(actualList, "Facility orders shouldn't be null");
        assertEquals(expectedOrders.get(0), actualList.get(0), "Expected 1 facility order");
        verify(sqlFacilityOrderDao, times(1)).findAll();
    }

    @Test
    void shouldGetFacilityOrderReturnEmptyListWhenNoOrdersExistTest() {
        // given

        // when
        when(sqlFacilityOrderDao.findAll()).thenReturn(Collections.emptyList());
        List<FacilityOrder> actualResult = facilityOrderService.getFacilityOrders();

        // then
        assertNotNull(actualResult, "Facility orders shouldn't be null");
        assertEquals(0, actualResult.size(), "Expected no facility orders");
        verify(sqlFacilityOrderDao, times(1)).findAll();
    }

    @Test
    void ShouldGetFacilityOrdersAndReturnSortedByDateTest() {
        // given
        FacilityOrder expectedFacilityOrder = TestObjectUtils.createTestFacilityOrder();
        List<FacilityOrder> expectedOrders = List.of(expectedFacilityOrder);

        // when
        when(sqlFacilityOrderDao.findFacilityOrdersSortedByDate()).thenReturn(expectedOrders);
        List<FacilityOrder> actualResult = facilityOrderService.getFacilityOrdersSortedByDate();

        // then
        assertNotNull(actualResult, "Facility orders shouldn't be null");
        assertEquals(expectedFacilityOrder, actualResult.get(0), "Expected 1 facility order");
        verify(sqlFacilityOrderDao, times(1)).findFacilityOrdersSortedByDate();
    }

    @Test
    void shouldGetFacilityOrdersSortedByDateAndReturnEmptyListWhenNoOrdersExistTest() {
        // given

        // when
        when(sqlFacilityOrderDao.findFacilityOrdersSortedByDate()).thenReturn(Collections.emptyList());
        List<FacilityOrder> result = facilityOrderService.getFacilityOrdersSortedByDate();

        // then
        assertNotNull(result, "Facility orders shouldn't be null");
        assertEquals(0, result.size(), "Expected no facility orders");
        verify(sqlFacilityOrderDao, times(1)).findFacilityOrdersSortedByDate();
    }

    private static Stream<Arguments> paginationAndSortingTestCases() {
        return Stream.of(
                Arguments.of(0, 5, "acs", "id"),
                Arguments.of(0, 5, "desc", "date"),
                Arguments.of(1, 5, "desc", "id"),
                Arguments.of(0, 10, "asc", "date"),
                Arguments.of(100, 0, "asc", "id")
        );
    }

    @ParameterizedTest
    @MethodSource("paginationAndSortingTestCases")
    void shouldGetFacilityOrdersWithPaginationAndSortingAndReturnPaginatedListTest(int page, int size, String direction, String field) {
        // given
        FacilityOrder expectedFacilityOrder = TestObjectUtils.createTestFacilityOrder();
        List<FacilityOrder> expectedOrders = List.of(expectedFacilityOrder, expectedFacilityOrder);

        // when
        when(sqlFacilityOrderDao.findAll(page, size, field, direction)).thenReturn(expectedOrders);
        List<FacilityOrder> actualResult = facilityOrderService.getFacilityOrders(page, size, field, direction);

        // then
        assertNotNull(actualResult, "Facility orders shouldn't be null");
        assertEquals(expectedOrders.get(0), actualResult.get(0), "Expected 1 facility order");
        verify(sqlFacilityOrderDao, times(1)).findAll(page, size, field, direction);
    }

    @ParameterizedTest
    @MethodSource("paginationAndSortingTestCases")
    void shouldGetFacilityOrdersWithPaginationAndSortingAndReturnEmptyListWhenNoOrdersExistTest(int page, int size, String direction, String field) {
        // given

        // when
        when(sqlFacilityOrderDao.findAll(page, size, field, direction)).thenReturn(Collections.emptyList());
        List<FacilityOrder> result = facilityOrderService.getFacilityOrders(page, size, field, direction);

        // then
        assertNotNull(result, "Facility orders shouldn't be null");
        assertEquals(0, result.size(), "Expected no facility orders");
        verify(sqlFacilityOrderDao, times(1)).findAll(page, size, field, direction);
    }

    @Test
    void shouldCountAllAndReturnCorrectValueTest() {
        // given

        // when
        when(sqlFacilityOrderDao.countAll()).thenReturn(5L);
        Long result = facilityOrderService.countAll();

        // then
        assertEquals(5L, result, "Count should match the expected value");
        verify(sqlFacilityOrderDao, times(1)).countAll();
    }
}
