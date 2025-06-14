package org.example.ui.builder;

import lombok.RequiredArgsConstructor;
import org.example.ui.Builder;
import org.example.ui.ItemName;
import org.example.ui.Menu;
import org.example.ui.MenuItem;
import org.example.ui.RootBuilder;
import org.example.ui.actions.Action;
import org.example.ui.actions.BookingAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("bookingMenuBuilder")
@RequiredArgsConstructor
public class BookingMenuBuilder implements Builder {

    private final Menu menu = new Menu("Booking menu: ");
    private final Map<String, BookingAction> actions;

    private RootBuilder rootMenuBuilder;
    private Action returnAction;

    @Autowired
    public void setRootMenuBuilder(@Qualifier("rootMenuBuilder") RootBuilder rootMenuBuilder) {
        this.rootMenuBuilder = rootMenuBuilder;
    }

    @Autowired
    public void setReturnAction(@Qualifier("returnAction") Action returnAction) {
        this.returnAction = returnAction;
    }

    @Override
    public void buildMenu() {
        List<MenuItem> menuItems = createMenuItems();
        menu.setMenuItems(menuItems);
    }


    @Override
    public Menu getMenu() {
        return menu;
    }

    private List<MenuItem> createMenuItems() {
        MenuItem settleItem = createMenuItem(ItemName.SETTLE_VISITOR_IN_ROOM,
                actions.get("settleVisitorInRoomAction"));

        MenuItem evictItem = createMenuItem(ItemName.EVICT_VISITOR_FROM_ROOM,
                actions.get("evictVisitorFromRoomAction"));

        MenuItem exportBookingsItem = createMenuItem(ItemName.EXPORT_BOOKINGS,
                actions.get("exportBookingsAction"));

        MenuItem getByIdItem = createMenuItem(ItemName.PRINT_BY_ID,
                actions.get("printBookingByIdAction"));

        MenuItem printAllItem = createMenuItem(ItemName.PRINT_ALL,
                actions.get("printAllBookingsAction"));

        MenuItem returnItem = createMenuItem(ItemName.RETURN, returnAction);

        return List.of(settleItem, evictItem, exportBookingsItem,
                getByIdItem, printAllItem, returnItem);
    }


    private MenuItem createMenuItem(ItemName name, Action action) {
        return new MenuItem(name, action, rootMenuBuilder.getMenu());
    }
}
