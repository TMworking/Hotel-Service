package org.example.model.enums;

public enum RoomStatus {
    FREE("free"),
    OCCUPIED("occupied"),
    REPAIRED("repaired"),
    SERVICED("serviced");

    RoomStatus(String status) {
        this.roomStatus = status;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    private final String roomStatus;
}
