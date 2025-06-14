package org.example.ui.builder;

import lombok.RequiredArgsConstructor;
import org.example.ui.Builder;
import org.example.ui.ItemName;
import org.example.ui.Menu;
import org.example.ui.MenuItem;
import org.example.ui.actions.Action;
import org.example.ui.RootBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RootMenuBuilder implements RootBuilder {

    private final Menu menu = new Menu("Root menu: ");

    private Map<String, Builder> builders;

    private Action returnAction;
    private Action importFromJsonAction;
    private Action exportToJsonAction;

    @Autowired
    public void setExportToJsonAction(@Qualifier("exportToJsonAction")Action exportToJsonAction) {
        this.exportToJsonAction = exportToJsonAction;
    }

    @Autowired
    public void setImportFromJsonAction(@Qualifier("importFromJsonAction") Action importFromJsonAction) {
        this.importFromJsonAction = importFromJsonAction;
    }

    @Autowired
    public void setReturnAction(@Qualifier("returnAction") Action returnAction) {
        this.returnAction = returnAction;
    }

    @Autowired
    public void setBuilders(@Lazy Map<String, Builder> buildersMap) {
        this.builders = buildersMap;
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
        builders.values().forEach(Builder::buildMenu);
        MenuItem bookingItem = createMenuItem(ItemName.BOOKING, returnAction,
                builders.get("bookingMenuBuilder").getMenu());

        MenuItem facilityItem = createMenuItem(ItemName.FACILITY, returnAction,
                builders.get("facilityMenuBuilder").getMenu());

        MenuItem facilityOrderItem = createMenuItem(ItemName.FACILITY_ORDER, returnAction,
                builders.get("facilityOrderMenuBuilder").getMenu());

        MenuItem roomItem = createMenuItem(ItemName.ROOM, returnAction,
                builders.get("roomMenuBuilder").getMenu());

        MenuItem visitorItem = createMenuItem(ItemName.VISITOR, returnAction,
                builders.get("visitorMenuBuilder").getMenu());

        MenuItem importAllItem = createMenuItem(ItemName.IMPORT_ALL,
                importFromJsonAction, menu);

        MenuItem exportAllItem = createMenuItem(ItemName.EXPORT_ALL,
                exportToJsonAction, menu);

        return  List.of(bookingItem, facilityItem, facilityOrderItem, roomItem,
                visitorItem, importAllItem, exportAllItem);
    }

    private MenuItem createMenuItem(ItemName name, Action action, Menu menu) {
        return new MenuItem(name, action, menu);
    }
}
