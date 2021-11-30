package ru.pcs.crowdfunding.client.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.pcs.crowdfunding.client.clients.AuthorizationServiceClient;
import ru.pcs.crowdfunding.client.repositories.PongRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PingServiceImpl implements PingService {

    private final PongRepository pongRepository;

    private final AuthorizationServiceClient authorizationServiceClient;

    @Override
    public String getPong() {
        String pong = pongRepository.getPong();

        Optional<String> authorizationPong = getAuthorizationServicePong();
        if (authorizationPong.isPresent()) {
            pong += "\n" + authorizationPong.get();
        }

        return pong;
    }

    private Optional<String> getAuthorizationServicePong() {
        log.info("calling AuthorizationServiceClient.ping()");

        try {
            ResponseEntity<String> response = authorizationServiceClient.ping();
            log.debug("response:\n{}", response);

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("call failed with status: {}", response.getStatusCode());
                return Optional.empty();
            }

            String body = response.getBody();
            if (body == null) {
                log.error("Response has no body");
                return Optional.empty();
            }

            return Optional.of(body);
        } catch (Exception e) {
            log.error("call failed with exception", e);
        }

        return Optional.empty();
    }
}
