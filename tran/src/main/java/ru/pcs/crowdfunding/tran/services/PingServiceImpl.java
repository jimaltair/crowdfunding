package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.tran.repositories.PongRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PingServiceImpl implements PingService {

    private final PongRepository pongRepository;

    @Override
    public String getPong() {
        log.info("Returning 'pong' from repository");
        return pongRepository.getPong();
    }
}
