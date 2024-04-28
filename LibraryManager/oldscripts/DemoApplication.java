package com.librarymanagement.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.twilio.Twilio;

@SpringBootApplication
public class DemoApplication {

	public static final String ACCOUNT_SID = "AC5850fac7447e82e9b02031bdf9c27aa3";
    public static final String AUTH_TOKEN = "3b7b69e2803c08f3f265345463a0b9fc";

	public static void main(String[] args) {
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		Database.Init();
		SpringApplication.run(DemoApplication.class, args);
	}

}
