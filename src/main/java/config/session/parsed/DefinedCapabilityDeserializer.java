package config.session.parsed;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.Map;

/**
 * кастомный десериализатор обеспечивающий правильное чтение записи вида <capability type="value"/>
 */
public class DefinedCapabilityDeserializer extends StdDeserializer<DefinedCapability> {

        public DefinedCapabilityDeserializer() {
            this(null);
        }

        public DefinedCapabilityDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public DefinedCapability deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException {
            JsonNode node = jp.getCodec().readTree(jp);

            Map.Entry<String,Object> entry=(Map.Entry)node.fields().next();
            String type=entry.getKey();
            Object value = ((TextNode)entry.getValue()).asText();
            return new DefinedCapability(type,value);

            //return new DefinedCapability(id, itemName, new User(userId, null));
        }
    }
