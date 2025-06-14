package org.example.ui;


public enum ItemName {

    BOOKING("Booking"),
    FACILITY_ORDER("Facility order"),
    FACILITY("Facility"),
    ROOM("Room"),
    VISITOR("Visitor"),
    RETURN("Return"),
    PRINT_BY_ID("Print by id"),
    CREATE("Create"),
    DELETE("Delete"),
    PRINT_ALL("Print all"),
    SETTLE_VISITOR_IN_ROOM("Settle visitor in room"),
    EVICT_VISITOR_FROM_ROOM("Evict visitor from room"),
    PRINT_FACILITY_ORDER_BY_DATE("Print facility orders sorted by date"),
    PRINT_FACILITY_ORDER_BY_PRICE("Print facility orders sorted by price"),
    PRINT_FREE_ROOM_ID_BY_TYPE("Print free room id by type"),
    PRINT_FREE_ROOMS("Print free rooms"),
    PRINT_FREE_ROOMS_BY_DATE("Print free rooms on date"),
    PRINT_LAST_ROOM_BY_ID_VISITORS("Print last visitors of room by id"),
    PRINT_ROOMS_BY_MAX_VISITORS("Print rooms sorted by max visitors"),
    PRINT_ROOMS_BY_PRICE("Print rooms sorted by price"),
    PRINT_ROOMS_BY_STARS("Print rooms sorted by stars"),
    PRINT_ROOMS_BY_RELEASE_DATE("Print rooms sorted by release date"),
    PRINT_VISITOR_BY_ID_PAYMENT("Print visitor's payment"),
    PRINT_VISITORS_BY_NAME("Print visitors sorted by name"),
    EXPORT_ROOMS("Export rooms data in file"),
    IMPORT_ROOMS("Import rooms data from file"),
    EXPORT_VISITORS("Export visitors data in file"),
    IMPORT_VISITORS("Import visitors data from file"),
    EXPORT_FACILITIES("Export facilities data in file"),
    IMPORT_FACILITIES("Import facilities data from file"),
    EXPORT_BOOKINGS("Export bookings data in file"),
    IMPORT_BOOKINGS("Import bookings data from file"),
    EXPORT_FACILITY_ORDERS("Export facility orders data in file"),
    IMPORT_FACILITY_ORDERS("Import facility orders data from file"),
    CHANGE_ROOM_STATUS("Change room status"),
    IMPORT_ALL("Import all data"),
    EXPORT_ALL("Export all data");

    private final String name;

    public String getName() {
        return name;
    }

    ItemName(String name) {
        this.name = name;
    }
}
