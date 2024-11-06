package com.example.cloud;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.example.cloud.util.ColorTxt.writeInGreen;

@SpringBootApplication
public class CloudApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(CloudApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		writeInGreen("\n    *** App started and running... ***\n");
	}

	@Override
	public void run(String... args) {
	}
}