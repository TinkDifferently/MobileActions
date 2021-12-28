package io.dimension.config.pages;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.dimension.exceptions.ConfigurationException;
import io.dimension.utils.FileUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;

public class PageLoader {
    XmlMapper mapper;

    private PageLoader() {
        mapper = new XmlMapper();
    }

    private static PageLoader instance = new PageLoader();

    public static PageLoader getInstance() {
        return instance;
    }

    private void loadPage(File file) {
        try {
            XMLStreamReader sr = XMLInputFactory.newFactory().createXMLStreamReader(new FileInputStream(file));
            DefinedPage page = mapper.readValue(sr, DefinedPage.class);
            page.build();
        } catch (Exception e){
            throw new ConfigurationException("Не удалось прочитать страницы");
        }
    }

    public void loadPages() {
        FileUtils.filesFromPath(true, "src/main/resources/pages").forEach(this::loadPage);
    }
}
