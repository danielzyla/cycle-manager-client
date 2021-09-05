package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.ProductWriteDto;
import io.github.danielzyla.pdcaclient.model.ProductTableModel;
import io.github.danielzyla.pdcaclient.rest.ProductRestClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProductController implements Initializable {

    @FXML
    private BorderPane editProductBorderPane;
    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField productCodeTextField;
    @FXML
    private TextField productSerialNoTextField;
    @FXML
    private Button editButton;
    @FXML
    private Button cancelButton;

    private final ProductRestClient productRestClient;
    private String token;
    private ProductListViewController productListViewController;
    private ProductTableModel selectedProduct;
    private final static Pattern PATTERN = Pattern.compile("\\A(?!\\s*\\Z).+");

    public EditProductController() {
        this.productRestClient = new ProductRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeEditButton();
        initializeCancelButton();
    }

    private void initializeEditButton() {
        editButton.setOnAction(
                editAction -> {
                    if (validateProductName() && validateProductCode() && validateProductSerialNo()) {
                        ProductWriteDto productWriteDto = new ProductWriteDto();
                        productWriteDto.setId(selectedProduct.getId());
                        productWriteDto.setProductName(productNameTextField.getText());
                        productWriteDto.setProductCode(productCodeTextField.getText());
                        productWriteDto.setSerialNo(productSerialNoTextField.getText());
                        Thread thread = new Thread(() -> {
                            try {
                                productRestClient.updateProduct(
                                        getToken(),
                                        productWriteDto,
                                        () -> Platform.runLater(() -> {
                                            getStage().close();
                                            try {
                                                this.productListViewController.refreshProductList();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        })
                                );
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
                        thread.setDaemon(true);
                        thread.start();
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private boolean validateProductName() {
        Matcher matcher = PATTERN.matcher(productNameTextField.getText());
        if (matcher.find() && matcher.group().equals(productNameTextField.getText())) {
            return true;
        } else {
            validationAlert("product name");
            return false;
        }
    }

    private boolean validateProductCode() {
        Matcher matcher = PATTERN.matcher(productCodeTextField.getText());
        if (matcher.find() && matcher.group().equals(productCodeTextField.getText())) {
            return true;
        } else {
            validationAlert("product code");
            return false;
        }
    }

    private boolean validateProductSerialNo() {
        Matcher matcher = PATTERN.matcher(productSerialNoTextField.getText());
        if (matcher.find() && matcher.group().equals(productSerialNoTextField.getText())) {
            return true;
        } else {
            validationAlert("serial number");
            return false;
        }
    }

    private void validationAlert(String parameter) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Validate product " + parameter);
        alert.setContentText("Please enter valid " + parameter);
        alert.showAndWait();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(cancelAction -> getStage().close());
    }

    private Stage getStage() {
        return (Stage) editProductBorderPane.getScene().getWindow();
    }

    public void loadProjectWriteApiDto(ProductTableModel selectedProduct) {
        this.selectedProduct = selectedProduct;
        productNameTextField.setText(selectedProduct.getProductName());
        productCodeTextField.setText(selectedProduct.getProductCode());
        productSerialNoTextField.setText(selectedProduct.getSerialNo());
    }

    public void setController(ProductListViewController productListViewController) {
        this.productListViewController = productListViewController;
    }
}
