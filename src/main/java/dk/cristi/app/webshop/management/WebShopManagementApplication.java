package dk.cristi.app.webshop.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
@EnableEurekaClient
public class WebShopManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebShopManagementApplication.class, args);
	}
}
