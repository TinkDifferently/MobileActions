package config.session.parsed;

import exceptions.ConfigurationException;
import lombok.Data;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Collection;
import java.util.List;

/**
 * класс, используемый для описания конкретного устройства, на котором запускаются тесты
 * объекты класса являются автогенерируемыми
 */
@Data
public class DeviceInstance {
    /**
     * привязка устройства
     */
    String bind;
    /**
     * комбинированный тип устройства
     */
    String type;
    /**
     * прочитанные капабилки
     */
    DesiredCapabilities capabilities;

    public void setType(String type){
        this.type=type;
        this.capabilities=new DesiredCapabilities();
        String[] combinedType=this.type.split("\\s+");
        if (combinedType.length!=2)
            throw new ConfigurationException("Неверно определен тип устройства. Тип устройтва должен быть определен как ${platform} ${deviceType} (например android real)");
        Platform platform = DeviceConfig.getInstance().ofType(combinedType[0]);
        List<DefinedCapability> typedCapabilities;
        Platform.Area area=platform.getCommon();
        if (area!=null) {
            typedCapabilities = area.getCapabilities();
            if (typedCapabilities != null)
                typedCapabilities.forEach(o->this.capabilities.setCapability(o.getType(),o.getValue()));
        }
        switch (combinedType[1]){
            case "": return;
            case "real": area=platform.getReal(); break;
            case "virtual": area=platform.getVirtual(); break;
            default: throw  new ConfigurationException("Неизвестный тип устройства. Тип должен быть либо virtual, либо real");
        }
        if (area!=null) {
            typedCapabilities = area.getCapabilities();
            if (typedCapabilities != null)
                typedCapabilities.forEach(o->this.capabilities.setCapability(o.getType(),o.getValue()));
        }
    }

    /**
     * метод генерирующий полное описание тестируемого устройства
     * @param capabilities автогерируемый джексоном параметр
     */
    public void setCapabilities(Collection<DefinedCapability> capabilities){
        capabilities.forEach(o->this.capabilities.setCapability(o.getType(),o.getValue()));
    }

}
