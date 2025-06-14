package org.example.ui.builder;

import lombok.RequiredArgsConstructor;
import org.example.ui.Builder;
import org.example.ui.ItemName;
import org.example.ui.Menu;
import org.example.ui.MenuItem;
import org.example.ui.RootBuilder;
import org.example.ui.actions.Action;
import org.example.ui.actions.RoomAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RoomMenuBuilder implements Builder {

    private final Menu menu = new Menu("Room menu: ");
    private final Map<String, RoomAction> actions;

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
        MenuItem createItem = createMenuItem(ItemName.CREATE,
                actions.get("roomCreateAction"));

        MenuItem exportRoomsItem = createMenuItem(ItemName.EXPORT_ROOMS,
                actions.get("exportRoomsAction"));

        MenuItem setRoomStatusItem = createMenuItem(ItemName.CHANGE_ROOM_STATUS,
                actions.get("changeRoomStatusAction"));

        MenuItem getFreeRoomsItem = createMenuItem(ItemName.PRINT_FREE_ROOMS,
                actions.get("printFreeRoomsAction"));

        MenuItem getFreeRoomsByDateItem = createMenuItem(ItemName.PRINT_FREE_ROOMS_BY_DATE,
                actions.get("printFreeRoomsOnDateAction"));

        MenuItem getLastVisitorsItem = createMenuItem(ItemName.PRINT_LAST_ROOM_BY_ID_VISITORS,
                actions.get("printRoomLastVisitorsAction"));

        MenuItem roomPrintByPriceItem = createMenuItem(ItemName.PRINT_ROOMS_BY_PRICE,
                actions.get("printRoomsSortedByPriceAction"));

        MenuItem roomPrintByMaxVisitorsItem = createMenuItem(ItemName.PRINT_ROOMS_BY_MAX_VISITORS,
                actions.get("printRoomsSortedByType"));

        MenuItem getByIdItem = createMenuItem(ItemName.PRINT_BY_ID,
                actions.get("printRoomByIdAction"));

        MenuItem printAllItem = createMenuItem(ItemName.PRINT_ALL,
                actions.get("printAllRoomsAction"));

        MenuItem returnItem = createMenuItem(ItemName.RETURN,
                returnAction);

        return List.of(createItem,  exportRoomsItem,
                setRoomStatusItem, getFreeRoomsItem, getFreeRoomsByDateItem,
                getLastVisitorsItem, roomPrintByPriceItem, roomPrintByMaxVisitorsItem,
                getByIdItem, printAllItem, returnItem);
    }


    private MenuItem createMenuItem(ItemName name, Action action) {
        return  new MenuItem(name, action, rootMenuBuilder.getMenu());
    }
}
