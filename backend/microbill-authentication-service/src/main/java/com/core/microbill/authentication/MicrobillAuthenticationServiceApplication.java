package com.core.microbill.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.core.microbill.authentication")
public class MicrobillAuthenticationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrobillAuthenticationServiceApplication.class, args);
	}

}
