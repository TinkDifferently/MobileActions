package io.dimension.data;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class MapDataSource implements IDataSource {
    private final Map<String, Object> map;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        return (T) map.get(key);
    }
}
