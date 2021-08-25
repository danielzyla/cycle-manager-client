package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.DepartmentReadDto;
import io.github.danielzyla.pdcaclient.dto.ProductReadDto;
import io.github.danielzyla.pdcaclient.dto.ProjectWriteApiDto;
import io.github.danielzyla.pdcaclient.model.Department;
import io.github.danielzyla.pdcaclient.model.Product;
import io.github.danielzyla.pdcaclient.model.ProjectTableModel;
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

public class EditProjectController implements Initializable {

    @FXML
    private BorderPane editProjectBorderPane;
    @FXML
    private TextField projectNameTextField;
    @FXML
    private TextField projectCodeTextField;
    @FXML
    private Button departmentListButton;
    @FXML
    private Button productListButton;
    @FXML
    private ListView<DepartmentReadDto> departmentsListView;
    @FXML
    private ListView<ProductReadDto> productsListView;
    @FXML
    private Button editButton;
    @FXML
    private Button cancelButton;

    private final ProjectRestClient projectRestClient;
    private final DepartmentRestClient departmentRestClient;
    private final ProductRestClient productRestClient;
    private String token;
    private ProjectTableModel selectedProject;
    private MainController mainController;

    public EditProjectController() {
        this.departmentRestClient = new DepartmentRestClient();
        this.productRestClient = new ProductRestClient();
        this.projectRestClient = new ProjectRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeEditButton();
        loadDepartmentList();
        loadProductList();
        initializeCancelButton();
    }

    public void loadProjectWriteApiDto(ProjectTableModel selectedProject) {
        this.selectedProject = selectedProject;
        projectNameTextField.setText(selectedProject.getProjectName());
        projectCodeTextField.setText(selectedProject.getProjectCode());
    }

    private void initializeEditButton() {
        editButton.setOnAction(editAction -> {
            ProjectWriteApiDto projectWriteApiDto = new ProjectWriteApiDto();
            projectWriteApiDto.setId(selectedProject.getId());
            projectWriteApiDto.setProjectName(projectNameTextField.getText());
            projectWriteApiDto.setProjectCode(projectCodeTextField.getText());
            projectWriteApiDto.setDepartmentsIds(getDepartmentsIds());
            projectWriteApiDto.setProductsIds(getProductsIds());
            Thread thread = new Thread(() -> {
                try {
                    projectRestClient.updateProject(
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
        departmentListButton.setOnAction(loadDepartments -> {
            Thread thread = new Thread(() -> {
                try {
                    System.out.println(getToken());
                    List<DepartmentReadDto> departmentReadDtoList = departmentRestClient.getDepartments(getToken());
                    Platform.runLater(() -> {
                        departmentsListView.setItems(FXCollections.observableArrayList(departmentReadDtoList));
                        departmentsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                        ObservableList<DepartmentReadDto> items = departmentsListView.getItems();
                        for (DepartmentReadDto item : items) {
                            for (Department dept : selectedProject.getDepartments()) {
                                if(dept.getId() == item.getId()) {
                                    departmentsListView.getSelectionModel().select(item);
                                }
                            }

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        });
    }

    private void loadProductList() {
        productListButton.setOnAction(loadProducts -> {
            Thread thread = new Thread(() -> {
                try {
                    List<ProductReadDto> productReadDtoList = productRestClient.getProducts(getToken());
                    Platform.runLater(() -> {
                        productsListView.setItems(FXCollections.observableArrayList(productReadDtoList));
                        productsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                        ObservableList<ProductReadDto> items = productsListView.getItems();
                        for (ProductReadDto item : items) {
                            for (Product product : selectedProject.getProducts()) {
                                if(product.getId() == item.getId()) {
                                    productsListView.getSelectionModel().select(item);
                                }
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        });
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(cancelAction -> getStage().close());
    }

    private Stage getStage() {
        return (Stage) editProjectBorderPane.getScene().getWindow();
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
