package ru.vtb.app.chat.elements;

import io.dimension.elements.api.Button;
import io.dimension.elements.base.interfaces.Select;
import io.dimension.elements.base.interfaces.SelectableItem;
import io.dimension.elements.mobile.AbstractMobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.stream.Collectors;

public class PinCodeController extends AbstractMobileElement implements Select<PinCodeController.PinCodeItem> {

    static class PinCodeItem extends Button implements SelectableItem {
        public PinCodeItem(WebElement initialElement) {
            super(initialElement);
        }
    }

    public PinCodeController(By locator) {
        super(locator);
    }

    public PinCodeController(WebElement initialElement) {
        super(initialElement);
    }

    @Override
    public Collection<PinCodeItem> getOptions() {
        return $().getInitialElement().findElements(By.xpath(".//android.widget.Button")).stream().map(PinCodeItem::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}
