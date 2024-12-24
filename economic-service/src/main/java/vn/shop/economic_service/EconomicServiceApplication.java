package vn.shop.economic_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EconomicServiceApplication {

	public static void main(String[] args) {
		System.setProperty("aws.java.v1.printLocation", "true");
		System.setProperty("aws.java.v1.disableDeprecationAnnouncement", "true");
		SpringApplication.run(EconomicServiceApplication.class, args);
	}

}
