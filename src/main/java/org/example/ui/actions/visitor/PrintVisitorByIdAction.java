package org.example.ui.actions.visitor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.EntityNotFoundException;
import org.example.service.domain.VisitorService;
import org.example.ui.actions.VisitorAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintVisitorByIdAction implements VisitorAction {

    private final VisitorService visitorService;

    @Override
    public void execute() {
        try {
            Long entityId = UserInputs.inputId("Input entity id: ");
            System.out.println(visitorService.getById(entityId));
        } catch (EntityNotFoundException e) {
            log.error("Failed to find visitor: {}", e.getMessage(), e);
        }
    }
}
