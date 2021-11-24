package ru.pcs.crowdfunding.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.auth.repositories.PongRepository;

@Service
@RequiredArgsConstructor
public class PingServiceImpl implements PingService {

    private final PongRepository pongRepository;

    @Override
    public String getPong() {
        return pongRepository.getPong();
    }
}
