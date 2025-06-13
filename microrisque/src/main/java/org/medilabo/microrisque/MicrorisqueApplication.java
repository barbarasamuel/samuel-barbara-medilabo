package org.medilabo.microrisque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicrorisqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrorisqueApplication.class, args);
	}

}
