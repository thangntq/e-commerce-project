package com.shopme.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity","com.shopme.common.exception"})
public class ShopmeFontEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmeFontEndApplication.class, args);
	}

}
