package org.example.ui.builder;

import lombok.RequiredArgsConstructor;
import org.example.ui.Builder;
import org.example.ui.ItemName;
import org.example.ui.Menu;
import org.example.ui.MenuItem;
import org.example.ui.RootBuilder;
import org.example.ui.actions.Action;
import org.example.ui.actions.FacilityAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FacilityMenuBuilder implements Builder {

    private final Menu menu = new Menu("Facility menu: ");
    private final Map<String, FacilityAction> actions;

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
                actions.get("facilityCreateAction"));

        MenuItem exportFacilitiesItem = createMenuItem(ItemName.EXPORT_FACILITIES,
                actions.get("exportFacilityAction"));

        MenuItem getByIdItem = createMenuItem(ItemName.PRINT_BY_ID,
                actions.get("printFacilityByIdAction"));

        MenuItem printAllItem = createMenuItem(ItemName.PRINT_ALL,
                actions.get("printAllFacilitiesAction"));

        MenuItem returnItem = createMenuItem(ItemName.RETURN, returnAction);

        return List.of(createItem,  exportFacilitiesItem,
                getByIdItem, printAllItem, returnItem);
    }


    private MenuItem createMenuItem(ItemName name, Action action) {
        return  new MenuItem(name, action, rootMenuBuilder.getMenu());
    }
}
