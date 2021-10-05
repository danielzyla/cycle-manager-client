package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.model.Department;
import io.github.danielzyla.pdcaclient.model.EmployeeTableModel;
import io.github.danielzyla.pdcaclient.rest.EmployeeRestClient;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeListViewController implements Initializable {

    @FXML
    private BorderPane employeeListBorderPane;
    @FXML
    private ScrollPane employeeListScrollPane;
    @FXML
    private TableView<EmployeeTableModel> employeeListTableView;
    @FXML
    private GridPane employeeListGridPane;
    @FXML
    private Label nameLabel;
    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private String token;
    private final EmployeeRestClient employeeRestClient;

    public EmployeeListViewController() {
        this.employeeRestClient = new EmployeeRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.setText("Employee list");
        try {
            setEmployeeListTableView();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createButton.setOnAction(createProjectAction -> initializeCreateNewEmployeeStage());
        editButton.setOnAction(editAction -> initializeEditEmployeeStage());
        deleteButton.setOnAction(deleteAction -> initializeDeleteEmployeeStage());
    }

    @SuppressWarnings("unchecked")
    private void setEmployeeListTableView() throws InterruptedException {
        TableColumn<EmployeeTableModel, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<EmployeeTableModel, String> surnameColumn = new TableColumn<>("Surname");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

        TableColumn<EmployeeTableModel, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<EmployeeTableModel, Department> departmentColumn = new TableColumn<>("Department");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        departmentColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Department department, boolean empty) {
                super.updateItem(department, empty);
                if (department == null || empty) {
                    setText(null);
                } else {
                    setText(department.getDeptName());
                }
            }
        });

        employeeListTableView.getColumns().addAll(nameColumn, surnameColumn, emailColumn, departmentColumn);
        ObservableList<EmployeeTableModel> data = FXCollections.observableArrayList();
        loadEmployeeData(data);
        employeeListTableView.setItems(data);
    }

    private void loadEmployeeData(ObservableList<EmployeeTableModel> data) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                data.addAll(
                        employeeRestClient.getEmployees(getToken()).stream()
                                .map(EmployeeTableModel::new)
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

    private void initializeCreateNewEmployeeStage() {
        try {
            Stage createEmployeeStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/io/github/danielzyla/pdcaclient/fxml/add-employee.fxml"
            ));
            Parent parent = loader.load();
            Scene scene = new Scene(parent, 400, 400);
            createEmployeeStage.setScene(scene);
            createEmployeeStage.initModality(Modality.APPLICATION_MODAL);
            createEmployeeStage.initStyle(StageStyle.UNDECORATED);
            CreateEmployeeController controller = loader.getController();
            controller.setToken(getToken());
            controller.setController(this);
            createEmployeeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeEditEmployeeStage() {
        EmployeeTableModel selectedEmployee = employeeListTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try {
                Stage editEmployeeStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/edit-employee.fxml"
                ));
                EditEmployeeController controller = new EditEmployeeController(getToken(), selectedEmployee, this);
                loader.setController(controller);
                Parent parent = loader.load();
                Scene scene = new Scene(parent, 400, 500);
                editEmployeeStage.setScene(scene);
                editEmployeeStage.initModality(Modality.APPLICATION_MODAL);
                editEmployeeStage.initStyle(StageStyle.UNDECORATED);
                editEmployeeStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeDeleteEmployeeStage() {
        EmployeeTableModel selectedEmployee = employeeListTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try {
                Stage deleteEmployeeStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/delete-employee.fxml"
                ));
                Parent parent = loader.load();
                Scene scene = new Scene(parent, 400, 250);
                deleteEmployeeStage.setScene(scene);
                deleteEmployeeStage.initModality(Modality.APPLICATION_MODAL);
                deleteEmployeeStage.initStyle(StageStyle.UNDECORATED);
                DeleteEmployeeController controller = loader.getController();
                controller.setToken(getToken());
                controller.loadEmployeeData(selectedEmployee);
                controller.setController(this);
                deleteEmployeeStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void refreshProjectList() throws InterruptedException {
        employeeListTableView.getItems().clear();
        employeeListTableView.getColumns().clear();
        setEmployeeListTableView();
    }
}
