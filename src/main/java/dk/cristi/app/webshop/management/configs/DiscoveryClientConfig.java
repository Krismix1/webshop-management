package dk.cristi.app.webshop.management.configs;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("some")
@Configuration
@EnableEurekaClient
public class DiscoveryClientConfig {
}
