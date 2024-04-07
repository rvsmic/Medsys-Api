package com.medsys.medsysapi.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.medsys.medsysapi.api", "com.medsys.medsysapi.init", "com.medsys.medsysapi.db"})
public class MedsysApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MedsysApiApplication.class, args);
	}

}
