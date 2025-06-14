package org.example.model.enums;

public enum RoomType {
    BASIC("basic", 2, 1),
    DELUXE("deluxe", 3, 2),
    PRESIDENT("president", 4, 3);

    RoomType(String roomType, int maxVisitors, int starsCount) {
        this.roomType = roomType;
        this.maxVisitors = maxVisitors;
        this.starsCount = starsCount;
    }

    public int getMaxVisitors() {
        return maxVisitors;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getStarsCount() {
        return starsCount;
    }

    private String roomType;
    private int maxVisitors;
    private int starsCount;
}
