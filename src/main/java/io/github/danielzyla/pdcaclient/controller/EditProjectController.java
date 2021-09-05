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
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProjectController implements Initializable {

    @FXML
    private BorderPane editProjectBorderPane;
    @FXML
    private TextField projectNameTextField;
    @FXML
    private TextField projectCodeTextField;
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
    private final String token;
    private final ProjectTableModel selectedProject;
    private final ProjectListViewController projectListViewController;
    private final static Pattern PATTERN = Pattern.compile("\\A(?!\\s*\\Z).+");

    public EditProjectController(
            String token,
            ProjectTableModel selectedProject,
            ProjectListViewController projectListViewController
    ) {
        this.departmentRestClient = new DepartmentRestClient();
        this.productRestClient = new ProductRestClient();
        this.projectRestClient = new ProjectRestClient();
        this.token = token;
        this.selectedProject = selectedProject;
        this.projectListViewController = projectListViewController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProjectWriteApiDto();
        loadDepartmentList();
        loadProductList();
        initializeEditButton();
        initializeCancelButton();
    }

    public void loadProjectWriteApiDto() {
        projectNameTextField.setText(selectedProject.getProjectName());
        projectCodeTextField.setText(selectedProject.getProjectCode());
    }

    private void loadDepartmentList() {
        Thread thread = new Thread(() -> {
            try {
                List<DepartmentReadDto> departmentReadDtoList = departmentRestClient.getDepartments(this.token);
                Platform.runLater(() -> {
                    departmentsListView.setItems(FXCollections.observableArrayList(departmentReadDtoList));
                    departmentsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                    ObservableList<DepartmentReadDto> items = departmentsListView.getItems();
                    for (DepartmentReadDto item : items) {
                        for (Department dept : selectedProject.getDepartments()) {
                            if (dept.getId() == item.getId()) {
                                departmentsListView.getSelectionModel().select(item);
                            }
                        }
                    }
                });
            } catch (IOException e) {
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

    private void loadProductList() {
        Thread thread = new Thread(() -> {
            try {
                List<ProductReadDto> productReadDtoList = productRestClient.getProducts(this.token);
                Platform.runLater(() -> {
                    productsListView.setItems(FXCollections.observableArrayList(productReadDtoList));
                    productsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                    ObservableList<ProductReadDto> items = productsListView.getItems();
                    for (ProductReadDto item : items) {
                        for (Product product : selectedProject.getProducts()) {
                            if (product.getId() == item.getId()) {
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
    }

    private void initializeEditButton() {
        editButton.setOnAction(editAction -> {
            if (validateProjectName() && validateProjectCode()) {
                ProjectWriteApiDto projectWriteApiDto = new ProjectWriteApiDto();
                projectWriteApiDto.setId(selectedProject.getId());
                projectWriteApiDto.setProjectName(projectNameTextField.getText());
                projectWriteApiDto.setProjectCode(projectCodeTextField.getText());
                projectWriteApiDto.setDepartmentsIds(getDepartmentsIds());
                projectWriteApiDto.setProductsIds(getProductsIds());
                Thread thread = new Thread(() -> {
                    try {
                        projectRestClient.updateProject(
                                this.token,
                                projectWriteApiDto,
                                () -> Platform.runLater(() -> {
                                    getStage().close();
                                    try {
                                        this.projectListViewController.refreshProjectList();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                })
                        );
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
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

    private boolean validateProjectName() {
        Matcher matcher = PATTERN.matcher(projectNameTextField.getText());
        if (matcher.find() && matcher.group().equals(projectNameTextField.getText())) {
            return true;
        } else {
            validationAlert("project name");
            return false;
        }
    }

    private boolean validateProjectCode() {
        Matcher matcher = PATTERN.matcher(projectCodeTextField.getText());
        if (matcher.find() && matcher.group().equals(projectCodeTextField.getText())) {
            return true;
        } else {
            validationAlert("project code");
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

    private Stage getStage() {
        return (Stage) editProjectBorderPane.getScene().getWindow();
    }
}
