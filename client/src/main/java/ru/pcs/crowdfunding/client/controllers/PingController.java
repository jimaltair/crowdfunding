package ru.pcs.crowdfunding.client.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pcs.crowdfunding.client.services.PingService;

@RestController
@RequestMapping("/api/v0/ping")
@RequiredArgsConstructor
@Slf4j
public class PingController {

    private final PingService pingService;

    @GetMapping
    public String doGet() {
        log.info("Accessing http://localhost:8080/api/v0/ping. Result: {}", pingService.getPong());
        return pingService.getPong();
    }
}
