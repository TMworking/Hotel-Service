package org.example.ui.actions.facility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.EntityNotFoundException;
import org.example.service.domain.FacilityService;
import org.example.ui.actions.FacilityAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintFacilityByIdAction implements FacilityAction {

    private final FacilityService facilityService;

    @Override
    public void execute() {
        try {
            Long entityId = UserInputs.inputId("Input entity id: ");
            System.out.println(facilityService.getById(entityId));
        } catch (EntityNotFoundException e) {
            log.error("Failed to find facility: {}", e.getMessage(), e);
        }
    }
}
