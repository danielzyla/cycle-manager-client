package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.handler.CrudOperationResultHandler;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class CycleRestClient {
    private static final String GET_CYCLES_URL_PATH = "/cycles";
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public CycleRestClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public void createNextCycle(
            String token,
            Long projectId,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<Long> request = new HttpEntity<>(projectId, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_CYCLES_URL_PATH,
                HttpMethod.POST,
                request,
                Void.class
        );
        if (responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        } else throw new RuntimeException("can't add next cycle to project having id: " + projectId);
    }
}
