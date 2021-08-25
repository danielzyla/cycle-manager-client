package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.ProductReadDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
}
