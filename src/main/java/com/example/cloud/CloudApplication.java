package com.example.cloud;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

import static com.example.cloud.util.Logger.logGreen;
import static com.example.cloud.util.Logger.initLog;

@SpringBootApplication
public class CloudApplication implements CommandLineRunner {

	public static void main(String[] args) throws IOException {
		SpringApplication app = new SpringApplication(CloudApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		initLog();
		logGreen("\n    *** App started and running... ***\n");
	}

	@Override
	public void run(String... args) {
	}
}