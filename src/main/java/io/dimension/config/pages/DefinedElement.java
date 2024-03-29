package io.dimension.config.pages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import io.dimension.utils.FileUtils;

@Data
class DefinedElement {
    @JsonIgnore
    private final static String description;
    static {
        description=FileUtils.read("src/main/resources/patterns/element.pattern");
    }
    private String androidPath;
    private String iosPath;
    private String bind;
    private String name;
    private String type;
    private boolean collection=false;
    private String androidStrategy="xpath";
    private String iosStrategy="iOSClassChain";


    String build() {
        return DefinedElement.description.replaceFirst("\\$elementBindPlaceHolder",bind)
                .replaceFirst("\\$elementNamePlaceHolder",name)
                .replaceAll("\\$elementTypePlaceHolder",type)
                .replaceFirst("\\$androidByPlaceHolder",String.format("MobileBy.%s(\"%s\")",androidStrategy,androidPath))
                .replaceFirst("\\$iosByPlaceHolder",String.format("MobileBy.%s(\"%s\")",iosStrategy,iosPath));
    }
}
