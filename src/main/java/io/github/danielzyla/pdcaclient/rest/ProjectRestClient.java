package io.github.danielzyla.pdcaclient.rest;


import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.ProjectReadDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProjectRestClient {
    private static final String GET_PROJECTS_PATH = "/projects";
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public ProjectRestClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public List<ProjectReadDto> getProjects(String token) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<ProjectReadDto> request = new HttpEntity<>(headers);
        ResponseEntity<ProjectReadDto[]> projects =
                restTemplate.exchange(
                        PropertyProvider.getRestAppUrl() + GET_PROJECTS_PATH,
                        HttpMethod.GET,
                        request,
                        ProjectReadDto[].class
                );
        return Arrays.asList(projects.getBody());
    }
}
