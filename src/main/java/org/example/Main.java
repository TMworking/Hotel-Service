package org.example;

import org.example.configs.AppConfig;
import org.example.ui.MenuController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MenuController menuController = context.getBean(MenuController.class);
        menuController.run();
    }
}