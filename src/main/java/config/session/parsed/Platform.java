package config.session.parsed;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;
import org.jetbrains.annotations.Contract;

import java.util.List;

@Data
class Platform {
    @Data
    static class Area {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JsonProperty("capability")
        List<DefinedCapability> capabilities=null;

        @Contract(pure = true)
        public Area(){

        }
    }
    String type;
    Area common;
    Area real;
    Area virtual;

}
