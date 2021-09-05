package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.ActPhaseReadDto;
import io.github.danielzyla.pdcaclient.dto.ActPhaseWriteApiDto;
import io.github.danielzyla.pdcaclient.handler.CrudOperationResultHandler;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class ActPhaseRestClient {
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private static final String GET_ACT_PHASES_URL_PATH = "/act_phases";

    public ActPhaseRestClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public ActPhaseReadDto getActPhaseById(String token, Long actPhaseId) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<Long> request = new HttpEntity<>(actPhaseId, headers);
        ResponseEntity<ActPhaseReadDto> actPhaseResponseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_ACT_PHASES_URL_PATH + "/" + actPhaseId,
                HttpMethod.GET,
                request,
                ActPhaseReadDto.class
        );
        return actPhaseResponseEntity.getBody();
    }

    public void updateActPhase(
            String token,
            ActPhaseWriteApiDto actPhaseWriteApiDto,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<ActPhaseWriteApiDto> request = new HttpEntity<>(actPhaseWriteApiDto, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_ACT_PHASES_URL_PATH,
                HttpMethod.PUT,
                request,
                Void.class
        );
        if(responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        }
    }

    public void createActPhaseTask(
            String token,
            long id,
            CrudOperationResultHandler handler
    ) throws InterruptedException, IOException {
        headers.setBearerAuth(token);
        HttpEntity<Long> request = new HttpEntity<>(id, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_ACT_PHASES_URL_PATH + "/add_task",
                HttpMethod.PUT,
                request,
                Void.class
        );
        if (responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        }
    }
}
