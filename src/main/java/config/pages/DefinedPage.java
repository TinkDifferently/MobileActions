package config.pages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import config.session.Config;
import lombok.Data;
import utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "page")
public class DefinedPage {
    @JsonIgnore
    private final static String description;

    static {
        description = FileUtils.read("src/main/resources/patterns/page.pattern");
    }

    private String bind;
    private String name;
    private List<DefinedElement> elements;

    public void build() {
        StringBuilder builder=new StringBuilder(DefinedPage.description.replaceFirst("\\$pageBindPlaceHolder",bind)
                .replaceFirst("\\$pageNamePlaceHolder", name));
        for (DefinedElement element : elements) {
            builder.append(element.build()).append('\n');
        }
        builder.append('}');
        FileUtils.write(Config.getGeneratedPagesLocation() + File.separator + name + ".java",builder.toString());
    }
}
