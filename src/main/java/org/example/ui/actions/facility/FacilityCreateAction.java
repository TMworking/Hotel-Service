package org.example.ui.actions.facility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.enums.FacilityType;
import org.example.service.domain.FacilityService;
import org.example.ui.actions.FacilityAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class FacilityCreateAction implements FacilityAction {

    private final FacilityService facilityService;

    @Override
    public void execute() {
        FacilityType facilityType = UserInputs.inputFacilityType("Input facility type (BUFFET, SWIMMING, CLEANING): ");
        BigDecimal cost = UserInputs.inputCost("Input facility cost: ");
        facilityService.saveFacility(cost, facilityType);;
    }
}
