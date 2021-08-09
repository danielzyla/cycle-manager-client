package io.github.danielzyla.pdcaclient.model;

import lombok.Data;

@Data
public class Product {
    private long id;
    private String productName, productCode, serialNo;
}
