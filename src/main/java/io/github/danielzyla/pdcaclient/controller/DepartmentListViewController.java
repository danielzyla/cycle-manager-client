package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.model.DepartmentTableModel;
import io.github.danielzyla.pdcaclient.rest.DepartmentRestClient;
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

public class DepartmentListViewController implements Initializable {

    @FXML
    private BorderPane departmentListBorderPane;
    @FXML
    private ScrollPane departmentListScrollPane;
    @FXML
    private TableView<DepartmentTableModel> departmentListTableView;
    @FXML
    private Label nameLabel;
    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private final DepartmentRestClient departmentRestClient;
    private String token;

    public DepartmentListViewController() {
        this.departmentRestClient = new DepartmentRestClient();
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.setText("Department list");
        setDepartmentListTableView();
        createButton.setOnAction(createDepartmentAction -> initializeCreateNewDepartmentStage());
        editButton.setOnAction(editAction -> initializeEditDepartmentStage());
        deleteButton.setOnAction(deleteAction -> initializeDeleteDepartmentStage());
    }


    private void setDepartmentListTableView() throws InterruptedException {
        TableColumn<DepartmentTableModel, String> departmentName = new TableColumn<>("Name");
        departmentName.setCellValueFactory(new PropertyValueFactory<>("deptName"));

        departmentListTableView.getColumns().addAll(departmentName);
        ObservableList<DepartmentTableModel> data = FXCollections.observableArrayList();
        loadDepartmentData(data);
        departmentListTableView.setItems(data);
    }

    private void loadDepartmentData(ObservableList<DepartmentTableModel> data) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                data.addAll(
                        departmentRestClient.getDepartments(getToken()).stream().
                                map(DepartmentTableModel::new)
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

    private void initializeDeleteDepartmentStage() {
        DepartmentTableModel selectedDepartment = departmentListTableView.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            try {
                Stage deleteDepartmentStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/delete-department.fxml"
                ));
                Parent parent = loader.load();
                Scene scene = new Scene(parent, 400, 250);
                deleteDepartmentStage.setScene(scene);
                deleteDepartmentStage.initModality(Modality.APPLICATION_MODAL);
                deleteDepartmentStage.initStyle(StageStyle.UNDECORATED);
                DeleteDepartmentController controller = loader.getController();
                controller.setToken(getToken());
                controller.loadDepartmentData(selectedDepartment);
                controller.setController(this);
                deleteDepartmentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeEditDepartmentStage() {
        DepartmentTableModel selectedDepartment = departmentListTableView.getSelectionModel().getSelectedItem();
        if (selectedDepartment != null) {
            try {
                Stage editDepartmentStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/edit-department.fxml"
                ));
                Parent parent = loader.load();
                Scene scene = new Scene(parent, 400, 300);
                editDepartmentStage.setScene(scene);
                editDepartmentStage.initModality(Modality.APPLICATION_MODAL);
                editDepartmentStage.initStyle(StageStyle.UNDECORATED);
                EditDepartmentController controller = loader.getController();
                controller.setToken(getToken());
                controller.loadDepartmentWriteDto(selectedDepartment);
                controller.setController(this);
                editDepartmentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeCreateNewDepartmentStage() {
        try {
            Stage createDepartmentStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/io/github/danielzyla/pdcaclient/fxml/add-department.fxml"
            ));
            Parent parent = loader.load();
            Scene scene = new Scene(parent, 400, 300);
            createDepartmentStage.setScene(scene);
            createDepartmentStage.initModality(Modality.APPLICATION_MODAL);
            createDepartmentStage.initStyle(StageStyle.UNDECORATED);
            CreateDepartmentController controller = loader.getController();
            controller.setToken(getToken());
            controller.setController(this);
            createDepartmentStage.show();
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
        departmentListTableView.getItems().clear();
        departmentListTableView.getColumns().clear();
        setDepartmentListTableView();
    }
}
