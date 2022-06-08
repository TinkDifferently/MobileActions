package ru.vtb.app.chat.data;

import io.dimension.data.IDataSource;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Context implements IDataSource {
    private String authStyle;
    private boolean isFirst;
    private String pinCode;
    private String otpCode;
    private String contour;
    private String clientType;

    public static Context context;

    public static void mount(Context context) {
        Context.context = context;
    }

    @Override
    public Object getData(String key) {
        switch (key) {
            case "Тип авторизации":
                return authStyle;
            case "Первый вход": {
                var isFirst = this.isFirst;
                this.isFirst = false;
                return isFirst;
            }
            case "Пин код":
                return pinCode;
            case "Код подтверждения":
                return otpCode;
            case "Контур":
                return contour;
            case "Тип клиента":
                return clientType;
            default:
                throw new RuntimeException("Never happens");
        }
    }
}
