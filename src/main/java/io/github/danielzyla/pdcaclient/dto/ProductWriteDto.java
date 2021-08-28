package io.github.danielzyla.pdcaclient.dto;

import lombok.Data;

@Data
public class ProductWriteDto {
    private long id;
    private String productName, productCode, serialNo;
}
