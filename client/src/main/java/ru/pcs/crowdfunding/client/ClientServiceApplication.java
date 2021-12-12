package ru.pcs.crowdfunding.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

/**
 * есть предложение использовать тут @EnableRetry
 */
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class ClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
	}

}