package org.example.ui.actions.facility;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.FacilityService;
import org.example.ui.actions.FacilityAction;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintAllFacilitiesAction implements FacilityAction {

    private final FacilityService facilityService;

    @Override
    public void execute() {
        System.out.println(facilityService.getFacilities());
    }
}
