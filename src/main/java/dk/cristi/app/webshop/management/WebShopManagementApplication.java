package dk.cristi.app.webshop.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class WebShopManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebShopManagementApplication.class, args);
	}
}
