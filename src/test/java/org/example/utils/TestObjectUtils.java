package org.example.utils;

import org.example.model.domain.Booking;
import org.example.model.domain.Facility;
import org.example.model.domain.FacilityOrder;
import org.example.model.domain.RefreshToken;
import org.example.model.domain.Role;
import org.example.model.domain.Room;
import org.example.model.domain.User;
import org.example.model.domain.Visitor;
import org.example.model.enums.FacilityType;
import org.example.model.enums.RoomStatus;
import org.example.model.enums.RoomType;
import org.example.model.enums.UserRole;
import org.example.web.dto.auth.RefreshTokenRequest;
import org.example.web.dto.booking.request.SettleRequest;
import org.example.web.dto.booking.response.BookingShortResponse;
import org.example.web.dto.facility.request.FacilityRequest;
import org.example.web.dto.facilityorder.request.FacilityOrderRequest;
import org.example.web.dto.room.request.RoomRequest;
import org.example.web.dto.user.request.UserLoginRequest;
import org.example.web.dto.user.request.UserRegisterRequest;
import org.example.web.dto.visitor.request.VisitorRequest;
import org.example.web.dto.visitor.response.VisitorResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TestObjectUtils {

    public static FacilityOrder createTestFacilityOrder() {
        FacilityOrder testFacilityOrder = new FacilityOrder();
        testFacilityOrder.setId(1L);
        testFacilityOrder.setFacility(createTestFacility());
        testFacilityOrder.setVisitor(createTestVisitor());
        testFacilityOrder.setDate(LocalDateTime.now());
        return testFacilityOrder;
    }

    public static Facility createTestFacility() {
        Facility testFacility = new Facility();
        testFacility.setId(1L);
        testFacility.setFacilityType(FacilityType.BUFFET);
        testFacility.setCost(BigDecimal.valueOf(100));
        return testFacility;
    }

    public static Booking createTestbooking() {
        Booking testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setRoom(createTestRoom());
        testBooking.setVisitor(createTestVisitor());
        testBooking.setSettleDate(LocalDateTime.now());
        testBooking.setDuration(5);
        return testBooking;
    }

    public static Room createTestRoom() {
        Room testRoom = new Room();
        testRoom.setId(1L);
        testRoom.setRoomNumber("101");
        testRoom.setPrice(BigDecimal.valueOf(100));
        testRoom.setStatus(RoomStatus.FREE);
        testRoom.setRoomType(RoomType.BASIC);
        return testRoom;
    }

    public static Visitor createTestVisitor() {
        Visitor testVisitor = new Visitor();
        testVisitor.setId(1L);
        testVisitor.setName("name");
        testVisitor.setSurname("surname");
        testVisitor.setPassport("passport");
        return testVisitor;
    }

    public static User createTestUser() {
        User user = new User();
        user.setUsername("user");
        user.setPassword("password");
        user.setRole(createTestRole());
        user.setIsEnabled(true);

        return user;
    }

    public static Role createTestRole() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(UserRole.USER);
        return role;
    }

    public static RefreshToken createTestRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(1L);
        refreshToken.setToken("token");
        refreshToken.setUser(createTestUser());
        return refreshToken;
    }

    public static String createAccessToken() {
        return "access-token";
    }

    public static UserLoginRequest createUserLoginRequest() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("username");
        request.setPassword("password");
        return request;
    }

    public static RefreshTokenRequest createRefreshTokenRequest() {
        return new RefreshTokenRequest("token");
    }

    public static UserRegisterRequest createUserRegisterRequest() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setUsername("user");
        request.setPassword("password");
        return request;
    }

    public static BookingShortResponse createBookingShortResponse() {
        Booking booking = createTestbooking();
        return new BookingShortResponse(booking.getId(), booking.getSettleDate(), booking.getDuration());
    }

    public static SettleRequest createSettleRequest() {
        SettleRequest request = new SettleRequest();
        request.setDuration(5);
        request.setSettleDate(LocalDateTime.now());
        request.setRoomId(1L);
        request.setVisitorId(1L);
        return request;
    }

    public static FacilityOrderRequest createFacilityOrderRequest() {
        FacilityOrderRequest request = new FacilityOrderRequest();
        request.setFacilityId(1L);
        request.setVisitorId(1L);
        request.setOrderDate(LocalDateTime.now());
        return request;
    }

    public static FacilityRequest createFacilityRequest() {
        FacilityRequest request = new FacilityRequest();
        request.setCost(BigDecimal.valueOf(100));
        request.setFacilityType(FacilityType.BUFFET);
        return request;
    }

    public static RoomRequest createRoomRequest() {
        RoomRequest request = new RoomRequest();
        request.setRoomNumber("101");
        request.setCost(BigDecimal.valueOf(100));
        request.setRoomType(RoomType.BASIC);
        return request;
    }

    public static VisitorRequest createVisitorRequest() {
        VisitorRequest request = new VisitorRequest();
        request.setName("name");
        request.setSurname("surname");
        request.setPassport("12345");
        return request;
    }
}
