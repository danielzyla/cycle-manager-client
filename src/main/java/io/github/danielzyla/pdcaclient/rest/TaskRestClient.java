package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.TaskWriteDto;
import io.github.danielzyla.pdcaclient.handler.CrudOperationResultHandler;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class TaskRestClient {
    private final static String TASKS_URL_PATH = "/tasks";
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public TaskRestClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public void updateTask(
            String token,
            TaskWriteDto taskWriteDto,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<TaskWriteDto> request = new HttpEntity<>(taskWriteDto, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + TASKS_URL_PATH,
                HttpMethod.PUT,
                request,
                Void.class
        );
        if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        }
    }

    public void deleteTask(String token, long id, CrudOperationResultHandler handler) {
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    PropertyProvider.getRestAppUrl() + TASKS_URL_PATH + "?id=" + id,
                    HttpMethod.DELETE,
                    request,
                    Void.class
            );
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                handler.handle();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
