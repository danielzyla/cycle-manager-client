package io.github.danielzyla.pdcaclient.rest;


import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.ProjectReadDto;
import io.github.danielzyla.pdcaclient.dto.ProjectWriteApiDto;
import io.github.danielzyla.pdcaclient.handler.CrudOperationResultHandler;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ProjectRestClient {
    private static final String GET_PROJECTS_URL_PATH = "/projects";
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public ProjectRestClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public List<ProjectReadDto> getProjects(String token) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ProjectReadDto[]> projects =
                restTemplate.exchange(
                        PropertyProvider.getRestAppUrl() + GET_PROJECTS_URL_PATH,
                        HttpMethod.GET,
                        request,
                        ProjectReadDto[].class
                );
        return Arrays.asList(Objects.requireNonNull(projects.getBody()));
    }

    public void saveProject(
            String token,
            ProjectWriteApiDto projectWriteApiDto,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<ProjectWriteApiDto> request = new HttpEntity<>(projectWriteApiDto, headers);
        ResponseEntity<ProjectReadDto> projectReadDtoResponseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_PROJECTS_URL_PATH,
                HttpMethod.POST,
                request,
                ProjectReadDto.class
        );
        if (projectReadDtoResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
            handler.handle();
        } else throw new RuntimeException("can't save data transfer object: " + projectWriteApiDto);
    }

    public void deleteProject(
            String token,
            Long projectId,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_PROJECTS_URL_PATH + "?id=" + projectId,
                HttpMethod.DELETE,
                request,
                Void.class
        );
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            handler.handle();
        }
    }

    public void updateProject(
            String token,
            ProjectWriteApiDto projectWriteApiDto,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<ProjectWriteApiDto> request = new HttpEntity<>(projectWriteApiDto, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_PROJECTS_URL_PATH,
                HttpMethod.PUT,
                request,
                Void.class
        );
        if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        }
    }

    public ProjectReadDto getById(String token, Long projectId) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<Long> request = new HttpEntity<>(projectId, headers);
        ResponseEntity<ProjectReadDto> projectReadDtoResponseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_PROJECTS_URL_PATH + "/" + projectId,
                HttpMethod.GET,
                request,
                ProjectReadDto.class
        );
        return projectReadDtoResponseEntity.getBody();
    }
}
