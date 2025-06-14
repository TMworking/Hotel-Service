package org.example.ui.actions.visitor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.VisitorService;
import org.example.ui.actions.VisitorAction;
import org.example.util.UserInputs;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VisitorCreateAction implements VisitorAction {

    private final VisitorService visitorService;

    @Override
    public void execute() {
        String name = UserInputs.inputString("Input visitor's name: ");
        String surname = UserInputs.inputString("Input visitor's surname: ");
        String passport = UserInputs.inputString("Input visitor's passport: ");;

        visitorService.saveVisitor(name, surname, passport);
    }
}
