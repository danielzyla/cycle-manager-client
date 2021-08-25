package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.DepartmentReadDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DepartmentRestClient {

    private final static String DEPARTMENTS_URL_PATH = "/departments";
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public DepartmentRestClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public List<DepartmentReadDto> getDepartments(String token) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<DepartmentReadDto[]> departmentResponseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + DEPARTMENTS_URL_PATH,
                HttpMethod.GET,
                request,
                DepartmentReadDto[].class
        );
        return Arrays.asList(Objects.requireNonNull(departmentResponseEntity.getBody()));
    }
}
