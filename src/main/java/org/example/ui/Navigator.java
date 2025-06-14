package org.example.ui;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Navigator {
    private Menu currentMenu;

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void printMenu() {
        System.out.println(currentMenu.getName());
        currentMenu.getMenuItems().stream()
                .map(item -> (currentMenu.getMenuItems().indexOf(item) + 1) + ". " + item.toString())
                .forEach(System.out::println);
    }

    public void navigate(int index) {
        List<MenuItem> menuItems = currentMenu.getMenuItems();
        MenuItem selectedItem = menuItems.get(index - 1);
        setCurrentMenu(selectedItem.getNextMenu());
        selectedItem.getAction().execute();
    }
}
