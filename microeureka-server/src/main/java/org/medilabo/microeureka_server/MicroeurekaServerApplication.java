package org.medilabo.microeureka_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MicroeurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroeurekaServerApplication.class, args);
	}

}
