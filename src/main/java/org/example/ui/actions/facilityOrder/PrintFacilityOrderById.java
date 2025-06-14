package org.example.ui.actions.facilityOrder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.EntityNotFoundException;
import org.example.service.domain.FacilityOrderService;
import org.example.ui.actions.FacilityOrderAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintFacilityOrderById implements FacilityOrderAction {

    private final FacilityOrderService facilityOrderService;

    @Override
    public void execute() {
        try {
            Long entityId = UserInputs.inputId("Input entity id: ");
            System.out.println(facilityOrderService.getById(entityId));
        } catch (EntityNotFoundException e) {
            log.error("Failed to find facility order: {}", e.getMessage(), e);
        }
    }
}
