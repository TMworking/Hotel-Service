package org.example.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
@RequiredArgsConstructor
public class MenuController {

    private final Navigator navigator;
    private RootBuilder builder;

    @Autowired
    public void setBuilder(@Qualifier("rootMenuBuilder") RootBuilder builder) {
        this.builder = builder;
    }

    public void run() {
        navigator.setCurrentMenu(builder.getMenu());
        this.builder.buildMenu();
        String prompt = "Input action number: ";

        while (true) {
            this.navigator.printMenu();
            int menuSize = navigator.getCurrentMenu().getMenuItems().size();
            System.out.println(prompt);
            Scanner scanner = new Scanner(System.in);
            String itemIndex = scanner.next().trim();
            while (!itemIndex.matches("\\d+") || (Integer.parseInt(itemIndex) > menuSize) ||
                    (Integer.parseInt(itemIndex) < 1)) {
                System.out.println(prompt);
                itemIndex = scanner.nextLine().trim();
            }
            this.navigator.navigate(Integer.parseInt(itemIndex));
        }
    }
}
