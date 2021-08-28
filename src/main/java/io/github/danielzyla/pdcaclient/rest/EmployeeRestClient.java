package io.github.danielzyla.pdcaclient.rest;


import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.EmployeeReadDto;
import io.github.danielzyla.pdcaclient.dto.EmployeeWriteApiDto;
import io.github.danielzyla.pdcaclient.handler.CrudOperationResultHandler;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EmployeeRestClient {
    private static final String GET_EMPLOYEES_URL_PATH = "/employees";
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public EmployeeRestClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public List<EmployeeReadDto> getEmployees(String token) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<EmployeeReadDto> request = new HttpEntity<>(headers);
        ResponseEntity<EmployeeReadDto[]> employees =
                restTemplate.exchange(
                        PropertyProvider.getRestAppUrl() + GET_EMPLOYEES_URL_PATH,
                        HttpMethod.GET,
                        request,
                        EmployeeReadDto[].class
                );
        return Arrays.asList(Objects.requireNonNull(employees.getBody()));
    }

    public void saveEmployee(
            String token,
            EmployeeWriteApiDto employeeWriteApiDto,
            CrudOperationResultHandler handler
    ) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<EmployeeWriteApiDto> request = new HttpEntity<>(employeeWriteApiDto, headers);
        ResponseEntity<EmployeeReadDto> employeeReadDtoResponseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_EMPLOYEES_URL_PATH,
                HttpMethod.POST,
                request,
                EmployeeReadDto.class
        );
        if (employeeReadDtoResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
            handler.handle();
        } else throw new RuntimeException("can't save data transfer object: " + employeeWriteApiDto);
    }

    public void deleteEmployee(String token, Long employeeId, CrudOperationResultHandler handler) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_EMPLOYEES_URL_PATH + "?id=" + employeeId,
                HttpMethod.DELETE,
                request,
                Void.class
        );
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            handler.handle();
        }
    }

    public void updateEmployee(
            String token,
            EmployeeWriteApiDto employeeWriteApiDto,
            CrudOperationResultHandler handler
    ) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<EmployeeWriteApiDto> request = new HttpEntity<>(employeeWriteApiDto, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + GET_EMPLOYEES_URL_PATH,
                HttpMethod.PUT,
                request,
                Void.class
        );
        if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        }
    }
}
