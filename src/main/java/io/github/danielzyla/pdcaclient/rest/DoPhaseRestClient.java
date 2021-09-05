package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.DoPhaseReadDto;
import io.github.danielzyla.pdcaclient.dto.DoPhaseWriteApiDto;
import io.github.danielzyla.pdcaclient.handler.CrudOperationResultHandler;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class DoPhaseRestClient {
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private static final String GET_DO_PHASES_URL_PATH = "/do_phases";

    public DoPhaseRestClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public DoPhaseReadDto getDoPhaseById(String token, Long doPhaseId) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<Long> request = new HttpEntity<>(doPhaseId, headers);
        ResponseEntity<DoPhaseReadDto> doPhaseResponseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_DO_PHASES_URL_PATH + "/" + doPhaseId,
                HttpMethod.GET,
                request,
                DoPhaseReadDto.class
        );
        return doPhaseResponseEntity.getBody();
    }

    public void updateDoPhase(
            String token,
            DoPhaseWriteApiDto doPhaseWriteApiDto,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<DoPhaseWriteApiDto> request = new HttpEntity<>(doPhaseWriteApiDto, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_DO_PHASES_URL_PATH,
                HttpMethod.PUT,
                request,
                Void.class
        );
        if(responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        }
    }

    public void createDoPhaseTask(
            String token,
            Long id,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<Long> request = new HttpEntity<>(id, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_DO_PHASES_URL_PATH + "/add_task",
                HttpMethod.PUT,
                request,
                Void.class
        );
        if (responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        }
    }
}
