package ru.pcs.crowdfunding.tran.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.tran.repositories.PongRepository;

@Service
@RequiredArgsConstructor
public class PingServiceImpl implements PingService {

    private final PongRepository pongRepository;

    @Override
    public String getPong() {
        return pongRepository.getPong();
    }
}
