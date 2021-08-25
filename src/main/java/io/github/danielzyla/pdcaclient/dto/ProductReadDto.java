package io.github.danielzyla.pdcaclient.dto;

import lombok.Data;

@Data
public class ProductReadDto {
    private long id;
    private String productName;

    @Override
    public String toString() {
        return productName;
    }
}
