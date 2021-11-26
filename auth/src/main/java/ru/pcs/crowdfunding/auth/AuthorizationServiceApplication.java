package ru.pcs.crowdfunding.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AuthorizationServiceApplication {

    public static void main(String[] args) {
        log.info("Before Starting application");
        SpringApplication.run(AuthorizationServiceApplication.class, args);
        log.debug("Starting application in debug with {} arguments", args.length);
        log.info("Starting application with {} arguments", args.length);
    }

}
