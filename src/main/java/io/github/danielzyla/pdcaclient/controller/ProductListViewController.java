package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.model.ProductTableModel;
import io.github.danielzyla.pdcaclient.rest.ProductRestClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProductListViewController implements Initializable {

    @FXML
    private BorderPane productListBorderPane;
    @FXML
    private ScrollPane productListScrollPane;
    @FXML
    private TableView<ProductTableModel> productListTableView;
    @FXML
    private Label nameLabel;
    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private final ProductRestClient productRestClient;
    private String token;

    public ProductListViewController() {
        this.productRestClient = new ProductRestClient();
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.setText("Product list");
        setProductListTableView();
        createButton.setOnAction(createProductAction -> initializeCreateNewProductStage());
        editButton.setOnAction(editAction -> initializeEditProductStage());
        deleteButton.setOnAction(deleteAction -> initializeDeleteProductStage());
    }

    private void setProductListTableView() throws InterruptedException {
        TableColumn<ProductTableModel, String> productNameColumn = new TableColumn<>("Product name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

        TableColumn<ProductTableModel, String> productCodeColumn = new TableColumn<>("Product code");
        productCodeColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));

        TableColumn<ProductTableModel, String> productSerialNo = new TableColumn<>("Serial number");
        productSerialNo.setCellValueFactory(new PropertyValueFactory<>("serialNo"));

        productListTableView.getColumns().addAll(productNameColumn, productCodeColumn, productSerialNo);
        ObservableList<ProductTableModel> data = FXCollections.observableArrayList();
        loadProductData(data);
        productListTableView.setItems(data);
    }

    private void loadProductData(ObservableList<ProductTableModel> data) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                data.addAll(
                        productRestClient.getProducts(getToken()).stream()
                                .map(ProductTableModel::new)
                                .collect(Collectors.toList())
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
        thread.join();
    }

    private void initializeEditProductStage() {
        ProductTableModel selectedProduct = productListTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                Stage editProductStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/edit-product.fxml"
                ));
                Parent parent = loader.load();
                Scene scene = new Scene(parent, 400, 300);
                editProductStage.setScene(scene);
                editProductStage.initModality(Modality.APPLICATION_MODAL);
                editProductStage.initStyle(StageStyle.UNDECORATED);
                EditProductController controller = loader.getController();
                controller.setToken(getToken());
                controller.loadProjectWriteApiDto(selectedProduct);
                controller.setController(this);
                editProductStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeDeleteProductStage() {
        ProductTableModel selectedProduct = productListTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                Stage deleteProductStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/delete-product.fxml"
                ));
                Parent parent = loader.load();
                Scene scene = new Scene(parent, 400, 250);
                deleteProductStage.setScene(scene);
                deleteProductStage.initModality(Modality.APPLICATION_MODAL);
                deleteProductStage.initStyle(StageStyle.UNDECORATED);
                DeleteProductController controller = loader.getController();
                controller.setToken(getToken());
                controller.loadProjectData(selectedProduct);
                controller.setController(this);
                deleteProductStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeCreateNewProductStage() {
        try {
            Stage createProductStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/io/github/danielzyla/pdcaclient/fxml/add-product.fxml"
            ));
            Parent parent = loader.load();
            Scene scene = new Scene(parent, 400, 300);
            createProductStage.setScene(scene);
            createProductStage.initModality(Modality.APPLICATION_MODAL);
            createProductStage.initStyle(StageStyle.UNDECORATED);
            CreateProductController controller = loader.getController();
            controller.setToken(getToken());
            controller.setController(this);
            createProductStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void refreshProductList() throws InterruptedException {
        productListTableView.getItems().clear();
        productListTableView.getColumns().clear();
        setProductListTableView();
    }
}
