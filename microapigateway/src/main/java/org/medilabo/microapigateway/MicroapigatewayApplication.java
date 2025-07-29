package org.medilabo.microapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MicroapigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroapigatewayApplication.class, args);
	}

}
