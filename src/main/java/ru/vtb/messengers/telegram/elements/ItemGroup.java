package ru.vtb.messengers.telegram.elements;

import io.dimension.config.session.DriverController;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

public class ItemGroup extends ElementGroup<Item> {
    public ItemGroup(By locator) {
        super(locator);
    }

    @Override
    public List<Item> getCollection() {
        if (collection == null || collection.isEmpty()) {
            collection = DriverController.getInstance().getDriver().findElements(byExecutor.getLocator()).stream().map(Item::new).collect(Collectors.toList());
        }
        return collection;
    }
}
