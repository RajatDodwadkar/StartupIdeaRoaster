package com.startup.roaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class
})
public class RoasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoasterApplication.class, args);
	}
}
