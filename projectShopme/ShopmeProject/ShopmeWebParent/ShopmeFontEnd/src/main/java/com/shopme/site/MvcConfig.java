package com.shopme.site;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Slf4j
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        exposeDirectory("category-images",registry);

        exposeDirectory("brand-logos",registry);

        exposeDirectory("product-images",registry);
        exposeDirectory("site-logo",registry);

    }

    private void exposeDirectory(String pathPatern, ResourceHandlerRegistry registry){
        Path path = Paths.get(pathPatern);
        String absolutePath = path.toFile().getAbsolutePath();
        String logicalPath = pathPatern.replace("..","") + "/**";

        registry.addResourceHandler(logicalPath)
                .addResourceLocations("file:/" + absolutePath + "/");
        log.info(absolutePath);
    }
}
