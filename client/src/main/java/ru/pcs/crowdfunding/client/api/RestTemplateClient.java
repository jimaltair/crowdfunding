package ru.pcs.crowdfunding.client.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.pcs.crowdfunding.client.dto.ResponseDto;

import java.text.MessageFormat;

public class RestTemplateClient {

    private final RestTemplate restTemplate;
    private final String remoteAddress;
    private final String token;
    private final ObjectMapper objectMapper;

    public RestTemplateClient(RestTemplateBuilder restTemplateBuilder, String remoteAddress,
                              String token, ObjectMapper objectMapper) {
        this.restTemplate = restTemplateBuilder.build();
        this.remoteAddress = remoteAddress;
        this.token = token;
        this.objectMapper = objectMapper;
    }

    protected <T> T get(String url, Class<T> responseType) {
        final String fullUrl = remoteAddress + url;
        ResponseEntity<ResponseDto> responseEntity = restTemplate.getForEntity(fullUrl, ResponseDto.class);
        validateNotError(responseEntity, fullUrl);

        ResponseDto responseDto = responseEntity.getBody();
        return responseDto.getDataAs(responseType, objectMapper);
    }

    protected <T> T get(String url, Class<T> responseType, Object... urlVariables) {
        final String fullUrl = remoteAddress + url;
        ResponseEntity<ResponseDto> responseEntity = restTemplate.getForEntity(fullUrl, ResponseDto.class, urlVariables);
        validateNotError(responseEntity, fullUrl);

        ResponseDto responseDto = responseEntity.getBody();
        return responseDto.getDataAs(responseType, objectMapper);
    }

    protected <T> T post(String url, Object request, Class<T> responseType) {
        final String fullUrl = remoteAddress + url;
        ResponseEntity<ResponseDto> responseEntity = restTemplate.postForEntity(fullUrl, request, ResponseDto.class);
        validateNotError(responseEntity, fullUrl);

        ResponseDto responseDto = responseEntity.getBody();
        return responseDto.getDataAs(responseType, objectMapper);
    }

    private void validateNotError(ResponseEntity<ResponseDto> responseEntity, String url) {
        HttpStatus status = responseEntity.getStatusCode();
        if (status.isError()) {
            throw new IllegalStateException(
                    MessageFormat.format("Request to {0} failed with HTTP-status: {1}", url, status));
        }

        ResponseDto responseDto = responseEntity.getBody();
        if (responseDto == null) {
            throw new IllegalStateException(
                    MessageFormat.format("Request to {0} returned empty body", url));
        }

        if (!responseDto.isSuccess()) {
            throw new IllegalStateException(
                    MessageFormat.format("Request to {0} failed with error: {1}", url, responseDto.getError()));
        }
    }
}
