package dk.cristi.app.webshop.management.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

// https://docs.spring.io/spring-boot/docs/2.0.0.RELEASE/reference/html/configuration-metadata.html#configuration-metadata-annotation-processor
// http://www.mdoninger.de/2015/05/16/completion-for-custom-properties-in-spring-boot.html
@ConfigurationProperties(prefix = "web-shop.management")
public class WebShopManagementConfigProperties {
    /**
     * Space-separated list of origins to allow to use the API. '*' allows all origins. When not set,
     * CORS support is disabled.
     */
    private List<String> allowedOrigins = new ArrayList<>();

    public List<String> getAllowedOrigins() {
        return this.allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
}
