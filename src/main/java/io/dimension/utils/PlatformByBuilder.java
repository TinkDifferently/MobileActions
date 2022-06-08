package io.dimension.utils;

import io.dimension.config.session.DriverController;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;

public class PlatformByBuilder {

    private final Platform platform;
    private By by;

    private PlatformByBuilder(){
        platform= DriverController.getInstance().getCurrentPlatform();
    }

    public static PlatformByBuilder create(){
        return new PlatformByBuilder();
    }

    public PlatformByBuilder iOS(By by){
        if (platform==Platform.IOS){
            this.by=by;
        }
        return this;
    }

    public PlatformByBuilder android(By by){
        if (platform==Platform.ANDROID){
            this.by=by;
        }
        return this;
    }

    public By build(){
        if (by==null){
            throw new RuntimeException("Bad setting");
        }
        return by;
    }
}
