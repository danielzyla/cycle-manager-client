package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.model.DepartmentTableModel;
import io.github.danielzyla.pdcaclient.rest.DepartmentRestClient;
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

public class DeleteDepartmentController implements Initializable {

    @FXML
    private BorderPane deleteDepartmentBorderPane;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label departmentNameLabel;

    private String token;
    private DepartmentListViewController departmentListViewController;
    private final DepartmentRestClient departmentRestClient;
    private int departmentId;

    public DeleteDepartmentController() {
        this.departmentRestClient = new DepartmentRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeDeleteButton();
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(deleteDepartmentAction -> {
            Thread thread = new Thread(() -> {
                try {
                    departmentRestClient.removeDepartment(getToken(), departmentId, () -> Platform.runLater(() -> {
                        getStage().close();
                        try {
                            departmentListViewController.refreshProductList();
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
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void loadDepartmentData(DepartmentTableModel selectedDepartment) {
        this.departmentId = selectedDepartment.getId();
        departmentNameLabel.setText("name: " + selectedDepartment.getDeptName());
    }

    public void setController(DepartmentListViewController departmentListViewController) {
        this.departmentListViewController = departmentListViewController;
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(cancelAction -> getStage().close());
    }

    private Stage getStage() {
        return (Stage) deleteDepartmentBorderPane.getScene().getWindow();
    }
}
