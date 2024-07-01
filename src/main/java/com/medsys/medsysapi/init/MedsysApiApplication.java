package com.medsys.medsysapi.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.medsys.medsysapi.*"})
public class MedsysApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedsysApiApplication.class, args);
	}
}
