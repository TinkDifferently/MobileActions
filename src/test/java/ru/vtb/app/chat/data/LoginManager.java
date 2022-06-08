package ru.vtb.app.chat.data;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LoginManager {

    private String k3DefaultLogin="20057842";

    private String k4DefaultLogin="18013277";

    public static String provideLogin(String contour, String type){
        switch (contour){
            case "k4":{
                switch (type){
                    default: return k4DefaultLogin;
                }
            }
            case "k3":{
                switch (type){
                    default: return k3DefaultLogin;
                }
            }
        }
        throw new RuntimeException(String.format("Unknown contour '%s'", contour));
    }
}
