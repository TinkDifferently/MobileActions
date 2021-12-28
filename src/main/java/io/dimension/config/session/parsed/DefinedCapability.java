package io.dimension.config.session.parsed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.Objects;

/**
 *
 * класс позволяющий описывать в xml капабилки в виде <capability type="value"/>
 * @param <T> на данный момент не используется
 */
@Data
@JsonDeserialize(using = DefinedCapabilityDeserializer.class)
@JsonRootName("capability")
public class DefinedCapability <T> {
    @JacksonXmlProperty(isAttribute = true)
    String type;
    @JacksonXmlProperty(isAttribute = true)
    T value;
    @JsonIgnore
    public Class getValueType(){
        return value.getClass();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefinedCapability<?> that = (DefinedCapability<?>) o;
        return type.equals(that.type) &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    public DefinedCapability(String type, T value){
        this.type=type;
        this.value=value;
    }
}
