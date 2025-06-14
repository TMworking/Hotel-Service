package org.example.util;

import org.example.model.enums.FacilityType;
import org.example.model.enums.RoomStatus;
import org.example.model.enums.RoomType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class UserInputs {

    public UserInputs() {
    }

    public static Long inputId(String prompt) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(prompt);
        String input = scanner.nextLine().trim();
        while (!input.matches("\\d+")) {
            System.out.println("Invalid input.");
            input = scanner.nextLine().trim();
        }
        return Long.parseLong(input);
    }

    public static RoomType inputRoomType(String prompt) {
        Scanner scanner = new Scanner(System.in);
        Set<String> validRoomTypes = Arrays.stream(RoomType.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        System.out.println(prompt);
        String input = scanner.nextLine().trim().toUpperCase();
        while (!validRoomTypes.contains(input)) {
            System.out.println("Invalid room type.");
            input = scanner.nextLine().trim().toUpperCase();
        }
        return RoomType.valueOf(input);
    }

    public static RoomStatus inputRoomStatus(String prompt) {
        Scanner scanner = new Scanner(System.in);
        Set<String> validRoomStatus = Arrays.stream(RoomStatus.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        System.out.println(prompt);
        String input = scanner.nextLine().trim().toUpperCase();
        while (!validRoomStatus.contains(input)) {
            System.out.println("Invalid room status.");
            input = scanner.nextLine().trim().toUpperCase();
        }
        return RoomStatus.valueOf(input);
    }

    public static LocalDateTime inputDate(String prompt) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(prompt);
        String input = scanner.nextLine().trim();
        while (!input.matches("\\d{4}-\\d{2}-\\d{2}")) {
            System.out.println("Invalid input.");
            input = scanner.nextLine().trim();
        }
        return LocalDate.parse(input, formatter).atStartOfDay();
    }

    public static Integer inputDuration(String prompt) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(prompt);
        String input = scanner.nextLine().trim();
        while (!input.matches("\\d+")) {
            System.out.println("Invalid input.");
            input = scanner.nextLine().trim();
        }
        return Integer.parseInt(input);
    }

    public static FacilityType inputFacilityType(String prompt) {
        Scanner scanner = new Scanner(System.in);
        Set<String> validFacilityTypes = Arrays.stream(FacilityType.values())
                .map(Enum::name)
                .collect(Collectors.toSet());

        System.out.println(prompt);
        String input = scanner.nextLine().trim().toUpperCase();
        while (!validFacilityTypes.contains(input)) {
            System.out.println("Invalid facility type.");
            input = scanner.nextLine().trim().toUpperCase();
        }
        return FacilityType.valueOf(input);
    }

    public static BigDecimal inputCost(String prompt) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(prompt);
        String input = scanner.nextLine().trim();
        while (!input.matches("\\d+")) {
            System.out.println("Invalid input.");
            input = scanner.nextLine().trim();
        }
        return new BigDecimal(input);
    }

    public static Long inputNumber(String prompt) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(prompt);
        String input = scanner.nextLine().trim();
        while (!input.matches("\\d+")) {
            System.out.println("Invalid input.");
            input = scanner.nextLine().trim();
        }
        return Long.parseLong(input);
    }

    public static String inputString(String prompt) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(prompt);
        String input = scanner.nextLine().trim();
        while (input.trim().isEmpty()) {
            System.out.println("Invalid input.");
            input = scanner.nextLine().trim();
        }
        return input;
    }
}
