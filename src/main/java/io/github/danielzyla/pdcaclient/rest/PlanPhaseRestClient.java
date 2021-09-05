package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.PlanPhaseReadDto;
import io.github.danielzyla.pdcaclient.dto.PlanPhaseWriteApiDto;
import io.github.danielzyla.pdcaclient.handler.CrudOperationResultHandler;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class PlanPhaseRestClient {
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private static final String GET_PLAN_PHASES_URL_PATH = "/plan_phases";

    public PlanPhaseRestClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public PlanPhaseReadDto getPlanPhaseById(String token, Long planPhaseId) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<Long> request = new HttpEntity<>(planPhaseId, headers);
        ResponseEntity<PlanPhaseReadDto> planPhaseResponseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_PLAN_PHASES_URL_PATH + "/" + planPhaseId,
                HttpMethod.GET,
                request,
                PlanPhaseReadDto.class
        );
        return planPhaseResponseEntity.getBody();
    }

    public void updatePlanPhase(
            String token,
            PlanPhaseWriteApiDto planPhaseWriteApiDto,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<PlanPhaseWriteApiDto> request = new HttpEntity<>(planPhaseWriteApiDto, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_PLAN_PHASES_URL_PATH,
                HttpMethod.PUT,
                request,
                Void.class
        );
        if(responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        }
    }
}
