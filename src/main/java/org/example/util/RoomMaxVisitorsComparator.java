package org.example.util;

import org.example.model.domain.Room;

import java.util.Comparator;

public class RoomMaxVisitorsComparator implements Comparator<Room> {
    @Override
    public int compare(Room o1, Room o2) {
        return o1.getMaxVisitors() - o2.getMaxVisitors();
    }
}
