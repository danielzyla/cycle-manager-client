package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.ProductReadDto;
import io.github.danielzyla.pdcaclient.dto.ProductWriteDto;
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

public class ProductRestClient {
    private final static String PRODUCTS_URL_PATH = "/products";
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;

    public ProductRestClient() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
    }

    public List<ProductReadDto> getProducts(String token) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<ProductReadDto[]> productResponseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + PRODUCTS_URL_PATH,
                HttpMethod.GET,
                request,
                ProductReadDto[].class
        );
        return Arrays.asList(Objects.requireNonNull(productResponseEntity.getBody()));
    }

    public ProductReadDto saveProduct(
            String token,
            ProductWriteDto productWriteDto,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<ProductWriteDto> request = new HttpEntity<>(productWriteDto, headers);
        ResponseEntity<ProductReadDto> productReadDtoResponseEntity = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + PRODUCTS_URL_PATH,
                HttpMethod.POST,
                request,
                ProductReadDto.class
        );
        if (productReadDtoResponseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
            handler.handle();
        }
        return productReadDtoResponseEntity.getBody();
    }

    public void removeProduct(String token, long productId, CrudOperationResultHandler handler) throws IOException {
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(
                    PropertyProvider.getRestAppUrl() + PRODUCTS_URL_PATH + "?id=" + productId,
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
                alert.setContentText("Unable to delete product! It is probably assigned to project");
                alert.showAndWait();
            });
        }
    }

    public void updateProduct(
            String token,
            ProductWriteDto productWriteDto,
            CrudOperationResultHandler handler
    ) throws IOException, InterruptedException {
        headers.setBearerAuth(token);
        HttpEntity<ProductWriteDto> request = new HttpEntity<>(productWriteDto, headers);
        ResponseEntity<Void> response = restTemplate.exchange(
                PropertyProvider.getRestAppUrl() + PRODUCTS_URL_PATH,
                HttpMethod.PUT,
                request,
                Void.class
        );
        if (response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
            handler.handle();
        }
    }
}
