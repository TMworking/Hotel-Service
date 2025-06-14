package org.example.ui.actions.facilityOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.FacilityOrderService;
import org.example.ui.actions.FacilityOrderAction;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintFacilityOrdersByDateAction implements FacilityOrderAction {

    private final FacilityOrderService facilityOrderService;

    @Override
    public void execute() {
        System.out.println(facilityOrderService.getFacilityOrdersSortedByDate());
    }
}
