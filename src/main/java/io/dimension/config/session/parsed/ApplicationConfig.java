package io.dimension.config.session.parsed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.dimension.exceptions.ConfigurationException;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.List;

/**
 * Класс оборачивающий взаимодействие с applications.xml
 * используется для получения capabilities, зависимых от типа устройства
 */
@Data
@JsonRootName("applications")
public class ApplicationConfig {
    private static ApplicationConfig instance;
    @Data
    private static class Application{
        @JacksonXmlProperty(isAttribute = true)
        String bind;
        @Data
        private static class ApplicationInfo{
            @JacksonXmlProperty(isAttribute = true)
            private String type;
            @JacksonXmlElementWrapper(useWrapping = false)
            @JsonProperty("capability")
            List<DefinedCapability> capabilities;
        }
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("appInfo")
        List<ApplicationInfo> infos;
    }
    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("application")
    List<Application> applications;

    /**
     * @return инстанс конфигуратора приложений
     */
    @JsonIgnore
    public static ApplicationConfig getInstance(){
        if (instance==null)
            try {
                URL url=ClassLoader.getSystemClassLoader().getResource("applications.xml");
                XmlMapper mapper = new XmlMapper();
                instance=mapper.readValue(url, ApplicationConfig.class);
            } catch (Exception e){
                throw new ConfigurationException("Не удалось получить описание девайса",e);
            }
        return instance;
    }

    /**
     * создает и возвращает полный список капабилок в соответствии с каскадом платформа->тип устройства->тип девайса->приложение
     * @param applicationBind идентификатор приложения
     * @param device девайс
     * @return капабилки для создания драйвера
     */
    public DesiredCapabilities forDevice(@NotNull String applicationBind, @NotNull DeviceInstance device){
        Application application=applications.stream().filter(o->o.getBind().equalsIgnoreCase(applicationBind)).findFirst().orElseThrow(ConfigurationException::new);
        String[] type=device.getType().split("\\s+");
        int maxMatches=0;
        Application.ApplicationInfo current=null;
        for (Application.ApplicationInfo info:application.getInfos()){
            String[] applicationType=info.getType().split("\\s+");
            if (applicationType.length>type.length)
                continue;
            int matches=0;
            for (int i=0;i<applicationType.length;i++){
                if (applicationType[i].equals(type[i]))
                    matches++;
                else break;
            }
            if (matches>maxMatches){
                maxMatches=matches;
                current=info;
            }
        }
        if (current==null)
            throw new ConfigurationException("Не существует приложения для данного устройства");
        DesiredCapabilities capabilities=device.getCapabilities();
        current.capabilities.forEach(o->capabilities.setCapability(o.getType(),o.getValue()));
        return capabilities;
    }
}
