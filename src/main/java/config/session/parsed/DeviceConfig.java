package config.session.parsed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import exceptions.ConfigurationException;
import lombok.Data;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;


/**
 * Конфигуратор устройств, отвечающий за чтение файля devices.xml
 */
@Data
@JsonRootName("devices")
public class DeviceConfig {
    @JsonProperty("platforms")
    List<Platform> supportedPlatforms;
    @JsonProperty("instances")
    List<DeviceInstance> supportedDevices;
    @JsonIgnore
    private static DeviceConfig instance;

    public DeviceConfig(){
        instance=this;
    }
    @JsonIgnore
    public static DeviceConfig getInstance(){
        if (instance==null)
        try {
            URL url=ClassLoader.getSystemClassLoader().getResource("devices.xml");
            XmlMapper mapper = new XmlMapper();
            instance=mapper.readValue(url, DeviceConfig.class);
        } catch (Exception e){
            throw new ConfigurationException("Не удалось получить описание девайса",e);
        }
        return instance;
    }

    /**
     * используется для склеивания капабилок инстанса устройства и его типа
     * @param type тип платформы
     * @return объект-платформа для заданного типа устройства
     */
    public Platform ofType(String type){
        return supportedPlatforms.stream().filter(o->o.getType().equalsIgnoreCase(type)).findFirst().orElseThrow(ConfigurationException::new);
    }

    /**
     * возвращает описание устройства, прочитанное из devices.xml
     * @param driverBind идентификатор устройства
     * @return объект-описание устройства
     */
    public DeviceInstance getDeviceInstance(String driverBind) {
        return supportedDevices.stream().filter(o->o.getBind().equalsIgnoreCase(driverBind)).findFirst().orElseThrow(ConfigurationException::new);
    }
}
