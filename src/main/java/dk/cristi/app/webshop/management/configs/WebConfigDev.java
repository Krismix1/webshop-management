package dk.cristi.app.webshop.management.configs;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Profile("dev")
@Configuration
public class WebConfigDev extends WebConfig {

    @Value("${web-shop.management.allowed-origins}")
    private String allowedOrigins;
    private static final Logger logger = LoggerFactory.getLogger(WebConfigDev.class);

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (StringUtils.isNotEmpty(allowedOrigins)) {
            logger.info("Adding allowed origins: " + allowedOrigins);
            // Allow CORS for Angular application run as a different application
            registry.addMapping("/**").allowedOrigins(allowedOrigins.split(" "));
        } else {
            logger.info("No allowed origins specified.");
        }
    }
}
