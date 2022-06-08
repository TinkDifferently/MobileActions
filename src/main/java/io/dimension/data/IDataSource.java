package io.dimension.data;

public interface IDataSource {
    <T> T getData(String key);
}
