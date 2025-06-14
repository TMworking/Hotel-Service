package org.example.ui.actions.facilityOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.EntityNotFoundException;
import org.example.model.domain.Booking;
import org.example.model.domain.Facility;
import org.example.model.domain.Visitor;
import org.example.service.domain.FacilityOrderService;
import org.example.service.domain.FacilityService;
import org.example.service.domain.VisitorService;
import org.example.ui.actions.FacilityOrderAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateFacilityOrderAction implements FacilityOrderAction {

    private final FacilityOrderService facilityOrderService;
    private final VisitorService visitorService;
    private final FacilityService facilityService;

    @Override
    public void execute() {
        try {
            System.out.println(visitorService.getVisitors());
            Long visitorId = UserInputs.inputId("Input visitor id: ");
            Visitor visitor = visitorService.getById(visitorId);

            System.out.println(facilityService.getFacilities());
            Long facilityId = UserInputs.inputId("Input facility id: ");
            Facility facility = facilityService.getById(facilityId);

            LocalDateTime orderDate = UserInputs.inputDate("Input order date by yyyy-MM-dd format: ");

            List<Booking> bookings = visitorService.getVisitorsBookings(visitor);
            Booking booking = bookings.get(bookings.size() - 1);

            if (orderDate.isAfter(booking.getSettleDate()) &&
                    orderDate.isBefore(booking.getSettleDate().plusDays(booking.getDuration()))) {
                facilityOrderService.saveFacilityOrder(facility, visitor, orderDate);
            } else {
                System.out.println("Failed to create facility order, visitor don't have booking on this date");
                log.debug("Failed to create facility order, visitor don't have booking on {}", orderDate);
            }
        } catch (EntityNotFoundException e) {
            log.debug("Failed to create facility order: {}", e.getMessage(), e);
        }
    }
}
