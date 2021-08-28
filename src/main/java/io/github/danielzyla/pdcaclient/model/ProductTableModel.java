package io.github.danielzyla.pdcaclient.model;

import io.github.danielzyla.pdcaclient.dto.ProductReadDto;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductTableModel {
    private final SimpleLongProperty id;
    private final SimpleStringProperty productName;
    private final SimpleStringProperty productCode;
    private final SimpleStringProperty serialNo;

    public ProductTableModel(ProductReadDto productReadDto) {
        this.id = new SimpleLongProperty(productReadDto.getId());
        this.productName = new SimpleStringProperty(productReadDto.getProductName());
        this.productCode = new SimpleStringProperty(productReadDto.getProductCode());
        this.serialNo = new SimpleStringProperty(productReadDto.getSerialNo());
    }

    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getProductCode() {
        return productCode.get();
    }

    public SimpleStringProperty productCodeProperty() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode.set(productCode);
    }

    public String getSerialNo() {
        return serialNo.get();
    }

    public SimpleStringProperty serialNoProperty() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo.set(serialNo);
    }
}
