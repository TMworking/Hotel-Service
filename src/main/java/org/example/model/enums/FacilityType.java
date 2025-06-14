package org.example.model.enums;

public enum FacilityType {
    BUFFET("buffet"),
    SWIMMING("swimming"),
    CLEANING("cleaning");

    FacilityType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    private String description;
}
