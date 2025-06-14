package org.example.ui.actions.visitor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.domain.VisitorService;
import org.example.ui.actions.VisitorAction;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrintAllVisitorsAction implements VisitorAction {

    private final VisitorService visitorService;

    @Override
    public void execute() {
        System.out.println(visitorService.getVisitors());
    }
}
