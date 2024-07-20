package com.mustafaz.JobPortal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class configures Spring MVC to expose (allow the clients to access) /photos/ directory
 * in the file system.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    private static final String UPLOAD_DIR = "photos";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory(UPLOAD_DIR, registry);
    }

    /**
     * Any request coming in for "/photos/**", it'll map over to the actual files on our given
     * file system to show or share or expose those given photos.
     */
    private void exposeDirectory(String uploadDir, ResourceHandlerRegistry registry) {
        Path path = Paths.get(uploadDir);

        registry.addResourceHandler("/" + uploadDir + "/**")
                .addResourceLocations("file:" + path.toAbsolutePath() + "/");
    }
}
