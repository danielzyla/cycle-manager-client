package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.ProductWriteDto;
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

public class CreateProductController implements Initializable {

    @FXML
    private BorderPane createProductBorderPane;
    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField productCodeTextField;
    @FXML
    private TextField productSerialNoTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private final ProductRestClient productRestClient;
    private String token;
    private ProductListViewController productListViewController;
    private final static Pattern PATTERN = Pattern.compile("\\A(?!\\s*\\Z).+");

    public CreateProductController() {
        this.productRestClient = new ProductRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeSaveButton();
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(saveProductAction -> {
            if (validateProductName() && validateProductCode() && validateProductSerialNo()) {
                ProductWriteDto productWriteDto = new ProductWriteDto();
                productWriteDto.setProductName(productNameTextField.getText());
                productWriteDto.setProductCode(productCodeTextField.getText());
                productWriteDto.setSerialNo(productSerialNoTextField.getText());
                Thread thread = new Thread(() -> {
                    try {
                        productRestClient.saveProduct(getToken(), productWriteDto, () -> Platform.runLater(() -> {
                            getStage().close();
                            try {
                                productListViewController.refreshProductList();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.setDaemon(true);
                thread.start();
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
        alert.setTitle("Validate " + parameter);
        alert.setContentText("Please enter valid " + parameter);
        alert.showAndWait();
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(cancelAction -> getStage().close());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private Stage getStage() {
        return (Stage) createProductBorderPane.getScene().getWindow();
    }

    public void setController(ProductListViewController productListViewController) {
        this.productListViewController = productListViewController;
    }
}
