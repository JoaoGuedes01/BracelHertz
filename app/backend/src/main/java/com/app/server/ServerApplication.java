package com.app.server;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.app.server.ServerApplication;

@SpringBootApplication
@EntityScan(basePackageClasses = { ServerApplication.class, Jsr310JpaConverters.class })
public class ServerApplication {

	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Lisbon"));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
