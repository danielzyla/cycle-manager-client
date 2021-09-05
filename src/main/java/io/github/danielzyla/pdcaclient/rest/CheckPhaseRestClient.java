package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.CheckPhaseReadDto;
import io.github.danielzyla.pdcaclient.dto.CheckPhaseWriteDto;
import io.github.danielzyla.pdcaclient.handler.CrudOperationResultHandler;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class CheckPhaseRestClient {
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private static final String GET_CHECK_PHASES_URL_PATH = "/check_phases";

    public CheckPhaseRestClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public CheckPhaseReadDto getCheckPhaseById(String token, Long checkPhaseId) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<Long> request = new HttpEntity<>(checkPhaseId, headers);
        ResponseEntity<CheckPhaseReadDto> checkPhaseResponseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_CHECK_PHASES_URL_PATH + "/" + checkPhaseId,
                HttpMethod.GET,
                request,
                CheckPhaseReadDto.class
        );
        return checkPhaseResponseEntity.getBody();
    }

    public void updateCheckPhase(
            String token,
            CheckPhaseWriteDto checkPhaseWriteDto,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<CheckPhaseWriteDto> request = new HttpEntity<>(checkPhaseWriteDto, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_CHECK_PHASES_URL_PATH,
                HttpMethod.PUT,
                request,
                Void.class
        );
        if(responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        }
    }
}
