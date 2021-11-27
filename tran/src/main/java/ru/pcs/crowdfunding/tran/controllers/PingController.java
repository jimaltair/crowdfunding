package ru.pcs.crowdfunding.tran.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pcs.crowdfunding.tran.services.PingService;

@RestController
@RequestMapping("/api/v0/ping")
@RequiredArgsConstructor
public class PingController {

    private final PingService pingService;

    @GetMapping
    public String doGet() {
        return pingService.getPong();
    }
}
