package ru.vtb.app.chat.tests.pack;

import io.dimension.BaseTest;
import io.dimension.actions.IAction;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import ru.vtb.app.chat.data.Context;

public abstract class VtbTest extends BaseTest {


    @Parameters({"authStyle", "clientType", "contour", "isFirst", "otpCode", "pinCode"})
    @BeforeTest()
    public void initContext(String authStyle, String clientType, String contour, boolean isFirst, String otpCode, String pinCode) {
        var context=Context.builder()
                .authStyle(authStyle)
                .clientType(clientType)
                .contour(contour)
                .isFirst(isFirst)
                .otpCode(otpCode)
                .pinCode(pinCode)
                .build();
        Context.mount(context);
    }

    protected IAction login = () -> {
        actions("Авторизация")
                .dataSource(Context.context,"Тип авторизации, Первый вход, " +
                        "Пин код, Код подтверждения, Контур, Тип клиента")
                .action("Авторизация")
                .build()
                .run();
    };
}

