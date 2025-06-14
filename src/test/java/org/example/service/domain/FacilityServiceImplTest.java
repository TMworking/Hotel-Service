package org.example.service.domain;

import org.example.dao.FacilityDao;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Facility;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FacilityServiceImplTest {

    @Mock
    private FacilityDao sqlFacilityDao;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    @Test
    void shouldGetByIdAndReturnFacilityWhenFacilityExistsTest() {
        // given
        Facility expectedFacility = TestObjectUtils.createTestFacility();

        // when
        when(sqlFacilityDao.findById(1L)).thenReturn(expectedFacility);
        Facility actualFacility = facilityService.getById(1L);

        // then
        assertNotNull(actualFacility, "Facility shouldn't be null");
        assertEquals(expectedFacility, actualFacility, "Returned facility should match the expected one");
        verify(sqlFacilityDao, times(1)).findById(1L);
    }

    @Test
    void shouldNotGetByIdAndThrowExceptionWhenFacilityNotFoundTest() {
        // given

        // when
        when(sqlFacilityDao.findById(anyLong())).thenReturn(null);

        // then
        assertThrows(EntityNotFoundException.class,
                () -> facilityService.getById(999L),
                "Should throw EntityNotFoundException when Facility not found");
        verify(sqlFacilityDao, times(1)).findById(999L);
    }

    @Test
    void shouldSaveAndReturnFacilityTest() {
        // given
        Facility expectedFacility = TestObjectUtils.createTestFacility();

        // when
        when(sqlFacilityDao.save(any(Facility.class))).thenReturn(expectedFacility);
        Facility actualFacility = facilityService.saveFacility(expectedFacility.getCost(), expectedFacility.getFacilityType());

        // then
        assertNotNull(actualFacility, "Saved facility shouldn't be null");
        assertEquals(expectedFacility, actualFacility, "Returned facility should match the expected one");
        verify(sqlFacilityDao, times(1)).save(any(Facility.class));
    }

    @Test
    void shouldSaveAllFacilitiesTest() {
        // given
        Facility expectedFacility = TestObjectUtils.createTestFacility();
        List<Facility> facilities = Collections.singletonList(expectedFacility);

        // when
        facilityService.saveAllFacilities(facilities);

        // then
        verify(sqlFacilityDao, times(1)).saveAll(facilities);
    }

    @Test
    void shouldDeleteAllFacilitiesTest() {
        // given
        // when
        facilityService.deleteAllFacilities();

        // then
        verify(sqlFacilityDao, times(1)).deleteAll();
    }

    @Test
    void shouldPrintAllFacilitiesSortedByCostTest() {
        // given
        Facility expectedFacility = TestObjectUtils.createTestFacility();
        List<Facility> expectedFacilities = Collections.singletonList(expectedFacility);

        // when
        when(sqlFacilityDao.findFacilitiesSortedByCost()).thenReturn(expectedFacilities);
        facilityService.printFacilitiesSortedByCost();

        // then
        verify(sqlFacilityDao, times(1)).findFacilitiesSortedByCost();
    }

    @Test
    void shouldGetAllFacilitiesAndReturnListTest() {
        // given
        Facility expectedFacility = TestObjectUtils.createTestFacility();
        List<Facility> expectedFacilities = Collections.singletonList(expectedFacility);

        // when
        when(sqlFacilityDao.findAll()).thenReturn(expectedFacilities);
        List<Facility> actualList = facilityService.getFacilities();

        // then
        assertNotNull(actualList, "Facility list shouldn't be null");
        assertEquals(expectedFacilities.get(0), actualList.get(0), "Expected 1 facility");
        verify(sqlFacilityDao, times(1)).findAll();
    }

    @Test
    void shouldGetFacilitiesAndReturnEmptyListWhenNoFacilitiesExistTest() {
        // give

        // when
        when(sqlFacilityDao.findAll()).thenReturn(Collections.emptyList());
        List<Facility> actualList = facilityService.getFacilities();

        // then
        assertNotNull(actualList, "Facility list shouldn't be null");
        assertTrue(actualList.isEmpty(), "Facility list should be empty");
        verify(sqlFacilityDao, times(1)).findAll();
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
    void shouldGetFacilitiesWithPaginationAndReturnPaginatedFacilityListTest(int page, int size) {
        // given
        Facility expectedFacility = TestObjectUtils.createTestFacility();
        List<Facility> expectedFacilities = Collections.singletonList(expectedFacility);

        // when
        when(sqlFacilityDao.findAll(page, size)).thenReturn(expectedFacilities);
        List<Facility> actualList = facilityService.getFacilities(page, size);

        // then
        assertNotNull(actualList, "Facility list shouldn't be null");
        assertEquals(expectedFacilities.get(0), actualList.get(0), "Expected 1 facility");
        verify(sqlFacilityDao, times(1)).findAll(page, size);
    }

    @ParameterizedTest
    @MethodSource("paginationTestCases")
    void shouldGetFacilitiesWithPaginationAndReturnEmptyListWhenNoFacilitiesExistTest(int page, int size) {
        // given

        // when
        when(sqlFacilityDao.findAll(page, size)).thenReturn(Collections.emptyList());
        List<Facility> actualList = facilityService.getFacilities(page, size);

        // then
        assertNotNull(actualList, "Facility list shouldn't be null");
        assertTrue(actualList.isEmpty(), "Facility list should be empty");
        verify(sqlFacilityDao, times(1)).findAll(page, size);
    }

    @Test
    void shouldCountAllReturnCorrectCountTest() {
        // given
        Long expectedCount = 5L;

        // when
        when(sqlFacilityDao.countAll()).thenReturn(expectedCount);
        Long actualCount = facilityService.countAll();

        // then
        assertEquals(expectedCount, actualCount, "Count should match the expected value");
        verify(sqlFacilityDao, times(1)).countAll();
    }
}
