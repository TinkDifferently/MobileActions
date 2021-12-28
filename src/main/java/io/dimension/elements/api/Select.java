package io.dimension.elements.api;

import io.dimension.elements.base.interfaces.SelectableItem;
import io.dimension.elements.mobile.AbstractMobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class Select extends AbstractMobileElement implements io.dimension.elements.base.interfaces.Select<SelectableItem> {
    public Select(WebElement initialElement) {
        super(initialElement);
    }

    public Select(By locator) {
        super(locator);
    }

    @Override
    public Collection<SelectableItem> getOptions() {
        var $=$().getInitialElement();
        if ($==null){
            return Collections.emptySet();
        }
        return $.findElements(By.xpath(".//*")).stream().map(Selectable::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}
