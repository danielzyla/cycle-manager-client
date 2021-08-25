package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.DepartmentReadDto;
import io.github.danielzyla.pdcaclient.dto.ProductReadDto;
import io.github.danielzyla.pdcaclient.dto.ProjectWriteApiDto;
import io.github.danielzyla.pdcaclient.rest.DepartmentRestClient;
import io.github.danielzyla.pdcaclient.rest.ProductRestClient;
import io.github.danielzyla.pdcaclient.rest.ProjectRestClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateProjectController implements Initializable {

    @FXML
    private BorderPane createProjectBorderPane;
    @FXML
    private TextField projectNameTextField;
    @FXML
    private TextField projectCodeTextField;
    @FXML
    private Button departmentsButton;
    @FXML
    private ListView<DepartmentReadDto> departmentsListView;
    @FXML
    private Button productsButton;
    @FXML
    private ListView<ProductReadDto> productsListView;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private final ProjectRestClient projectRestClient;
    private final DepartmentRestClient departmentRestClient;
    private final ProductRestClient productRestClient;
    private String token;
    private MainController mainController;

    public CreateProjectController() {
        this.departmentRestClient = new DepartmentRestClient();
        this.productRestClient = new ProductRestClient();
        this.projectRestClient = new ProjectRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeSaveButton();
        initializeDepartmentsButton();
        initializeProductsButton();
    }

    private void initializeDepartmentsButton() {
        departmentsButton.setOnAction(choiceAction -> {
            departmentsListView.setDisable(false);
            loadDepartmentList();
        });
    }

    private void initializeProductsButton() {
        productsButton.setOnAction(choiceAction -> {
            productsListView.setDisable(false);
            loadProductList();
        });
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(saveAction -> {
            ProjectWriteApiDto projectWriteApiDto = new ProjectWriteApiDto();
            projectWriteApiDto.setProjectName(projectNameTextField.getText());
            projectWriteApiDto.setProjectCode(projectCodeTextField.getText());
            projectWriteApiDto.setDepartmentsIds(getDepartmentsIds());
            projectWriteApiDto.setProductsIds(getProductsIds());
            Thread thread = new Thread(() -> {
                try {
                    projectRestClient.saveProject(
                            getToken(),
                            projectWriteApiDto,
                            () -> Platform.runLater(() -> {
                                getStage().close();
                                try {
                                    this.mainController.refreshProjectList();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            })
                    );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        });
    }

    private List<Integer> getDepartmentsIds() {
        ObservableList<DepartmentReadDto> selectedItems = departmentsListView.getSelectionModel().getSelectedItems();
        List<Integer> selectedDepartments = new ArrayList<>();
        for (DepartmentReadDto dto : selectedItems) {
            selectedDepartments.add(dto.getId());
        }
        return selectedDepartments;
    }

    private List<Long> getProductsIds() {
        ObservableList<ProductReadDto> selectedItems = productsListView.getSelectionModel().getSelectedItems();
        List<Long> selectedProducts = new ArrayList<>();
        for (ProductReadDto dto : selectedItems) {
            selectedProducts.add(dto.getId());
        }
        return selectedProducts;
    }

    private void loadDepartmentList() {
        Thread thread = new Thread(() -> {
            try {
                List<DepartmentReadDto> departmentReadDtoList = departmentRestClient.getDepartments(getToken());
                Platform.runLater(() -> {
                    departmentsListView.setItems(FXCollections.observableArrayList(departmentReadDtoList));
                    departmentsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void loadProductList() {
        Thread thread = new Thread(() -> {
            try {
                List<ProductReadDto> productReadDtoList = productRestClient.getProducts(getToken());
                Platform.runLater(() -> {
                    productsListView.setItems(FXCollections.observableArrayList(productReadDtoList));
                    productsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(cancelAction -> getStage().close());
    }

    private Stage getStage() {
        return (Stage) createProjectBorderPane.getScene().getWindow();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
