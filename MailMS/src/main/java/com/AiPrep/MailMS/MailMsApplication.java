package com.AiPrep.MailMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:message.properties")
@SpringBootApplication
public class MailMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailMsApplication.class, args);
	}

}
