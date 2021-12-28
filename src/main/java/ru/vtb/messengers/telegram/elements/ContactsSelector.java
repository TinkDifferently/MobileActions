package ru.vtb.messengers.telegram.elements;

import io.dimension.elements.mobile.ByExecutor;
import org.openqa.selenium.By;

import java.util.List;
import java.util.stream.Collectors;

public class ContactsSelector extends ElementGroup<Item> {
    public ContactsSelector(By locator) {
        super(locator);
    }

    @Override
    public ByExecutor $() {
        return byExecutor;
    }

    @Override
    protected List<Item> getCollection() {
        return $().getInitialElement().findElements(By.xpath("./*"))
                .stream()
                .map(ContactItem::new)
                .collect(Collectors.toList());
    }
}
