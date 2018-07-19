package dk.cristi.app.webshop.management.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Profile("dev")
@Configuration
public class WebConfigDev implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**"); // Allow CORS for Angular application run as a different application
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false)
                .favorParameter(false)
                .defaultContentType(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON, MediaType.ALL)
                .ignoreAcceptHeader(true);
    }
}
