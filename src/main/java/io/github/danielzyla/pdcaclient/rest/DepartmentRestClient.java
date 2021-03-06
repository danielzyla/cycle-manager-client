package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.DepartmentReadDto;
import io.github.danielzyla.pdcaclient.dto.DepartmentWriteDto;
import io.github.danielzyla.pdcaclient.handler.CrudOperationResultHandler;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
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

    public DepartmentReadDto saveDepartment(
            String token,
            DepartmentWriteDto departmentWriteDto,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<DepartmentWriteDto> request = new HttpEntity<>(departmentWriteDto, headers);
        ResponseEntity<DepartmentReadDto> departmentReadDtoResponseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + DEPARTMENTS_URL_PATH,
                HttpMethod.POST,
                request,
                DepartmentReadDto.class
        );
        if (departmentReadDtoResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
            handler.handle();
        }
        return departmentReadDtoResponseEntity.getBody();
    }

    public void removeDepartment(String token, int departmentId, CrudOperationResultHandler handler) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    PropertyProvider.getRestAppUrl() + DEPARTMENTS_URL_PATH + "?id=" + departmentId,
                    HttpMethod.DELETE,
                    request,
                    Void.class
            );
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                handler.handle();
            }
        } catch (HttpClientErrorException.Conflict | InterruptedException e) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Conflict !");
                alert.setHeaderText(null);
                alert.setContentText("Unable to delete department! It is most likely assigned to project");
                alert.showAndWait();
            });
        }
    }

    public void updateDepartment(
            String token,
            DepartmentWriteDto departmentWriteDto,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<DepartmentWriteDto> request = new HttpEntity<>(departmentWriteDto, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + DEPARTMENTS_URL_PATH,
                HttpMethod.PUT,
                request,
                Void.class
        );
        if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        }
    }
}
