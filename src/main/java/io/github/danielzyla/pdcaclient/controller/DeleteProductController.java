package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.model.ProductTableModel;
import io.github.danielzyla.pdcaclient.rest.ProductRestClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeleteProductController implements Initializable {

    @FXML
    private BorderPane deleteProductBorderPane;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label productCodeLabel;

    private final ProductRestClient productRestClient;
    private String token;
    private ProductListViewController productListViewController;
    private long productId;

    public DeleteProductController() {
        this.productRestClient = new ProductRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeDeleteButton();
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(deleteProductAction -> {
            Thread thread = new Thread(() -> {
                try {
                    productRestClient.removeProduct(getToken(), productId, () -> Platform.runLater(() -> {
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
        });
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void loadProjectData(ProductTableModel selectedProduct) {
        this.productId = selectedProduct.getId();
        productNameLabel.setText("name: " + selectedProduct.getProductName());
        productCodeLabel.setText(
                "code: " + selectedProduct.getProductCode() + ", serial: " + selectedProduct.getSerialNo()
        );
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(cancelAction -> getStage().close());
    }

    private Stage getStage() {
        return (Stage) deleteProductBorderPane.getScene().getWindow();
    }

    public void setController(ProductListViewController productListViewController) {
        this.productListViewController = productListViewController;
    }
}
