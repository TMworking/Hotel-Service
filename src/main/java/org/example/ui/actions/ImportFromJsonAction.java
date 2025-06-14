package org.example.ui.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.domain.Booking;
import org.example.model.domain.Facility;
import org.example.model.domain.FacilityOrder;
import org.example.model.domain.Room;
import org.example.model.domain.Visitor;
import org.example.service.domain.BookingService;
import org.example.service.domain.DeserializeService;
import org.example.service.domain.FacilityOrderService;
import org.example.service.domain.FacilityService;
import org.example.service.domain.RoomService;
import org.example.service.domain.VisitorService;
import org.example.util.UserInputs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component("importFromJsonAction")
@RequiredArgsConstructor
public class ImportFromJsonAction implements Action {

    private final DeserializeService deserializeService;
    private final RoomService roomService;
    private final VisitorService visitorService;
    private final FacilityService facilityService;
    private final BookingService bookingService;
    private final FacilityOrderService facilityOrderService;

    @Value("${jsonImportPath}")
    private String path;

    @Override
    @Transactional
    public void execute() {

        String answer = UserInputs.inputString("All previous tables data will be removed. Are you sure about that? (y/n)").toLowerCase();

        if (answer.equals("y")) {
            try {
                deleteAllEntities();

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                objectMapper.setDateFormat(df);

                String jsonData = deserializeService.getDataFromJson(path);

                List<Room> roomList = objectMapper.readValue(jsonData, new TypeReference<List<Room>>() { });
                List<Booking> bookings = getBookings(roomList);
                List<Visitor> visitors = getVisitors(bookings);
                List<FacilityOrder> facilityOrders = getFacilityOrders(visitors);
                List<Facility> facilities = getFacilities(facilityOrders);

                saveAllEntities(roomList, visitors, facilities, bookings, facilityOrders);
            } catch (JsonProcessingException e) {
                log.error("Error parsing json: ", e);
            } catch (IOException e) {
                log.error("Error reading json file: ", e);
            }
        }
    }

    private void saveAllEntities(List<Room> roomList, List<Visitor> visitors, List<Facility> facilities, List<Booking> bookings, List<FacilityOrder> facilityOrders) {
        roomService.saveAllRooms(roomList);
        visitorService.saveAllVisitors(visitors);
        facilityService.saveAllFacilities(facilities);
        bookingService.saveAllBookings(bookings);
        facilityOrderService.saveAllFacilityOrders(facilityOrders);
    }

    private static List<Facility> getFacilities(List<FacilityOrder> facilityOrders) {
        List<Facility> facilities = facilityOrders.stream()
                .map(FacilityOrder::getFacility)
                .distinct()
                .collect(Collectors.toList());
        return facilities;
    }

    private static List<FacilityOrder> getFacilityOrders(List<Visitor> visitors) {
        List<FacilityOrder> facilityOrders = visitors.stream()
                .flatMap(visitor -> visitor.getFacilityOrderList().stream()
                        .peek(facilityOrder -> facilityOrder.setVisitor(visitor)))
                .collect(Collectors.toList());
        return facilityOrders;
    }

    private static List<Visitor> getVisitors(List<Booking> bookings) {
        List<Visitor> visitors = bookings.stream()
                .map(Booking::getVisitor)
                .distinct()
                .collect(Collectors.toList());
        return visitors;
    }

    private static List<Booking> getBookings(List<Room> roomList) {
        List<Booking> bookings = roomList.stream()
                .flatMap(room -> room.getBookings().stream()
                        .peek(booking -> booking.setRoom(room)))
                .collect(Collectors.toList());
        return bookings;
    }

    private void deleteAllEntities() {
        bookingService.deleteAllBookings();
        facilityOrderService.deleteAllFacilityOrders();
        roomService.deleteAllRooms();
        visitorService.deleteAllVisitors();
        facilityService.deleteAllFacilities();
    }
}
