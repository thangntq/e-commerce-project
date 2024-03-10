package com.shopme.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

@Configuration
@Slf4j
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {


        exposeDirectory("user-photos",registry);

        exposeDirectory("../category-images",registry);

        exposeDirectory("../brand-logos",registry);
        String categoryImagesName = "../category-images" ;
        Path categoryImagesDir = Paths.get(categoryImagesName);
        String categoryPhotosPath = categoryImagesDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/category-images/**").addResourceLocations("file:/"+ categoryPhotosPath +"/");
        log.info(categoryPhotosPath);

        String brandLogosDirName = "../brand-logos";
        Path brandLogosDir = Paths.get(brandLogosDirName);
        String brandLogosPath = brandLogosDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/brand-logos/**").addResourceLocations("file:/"+brandLogosPath+"/");
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
