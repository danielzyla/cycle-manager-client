package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.model.EmployeeTableModel;
import io.github.danielzyla.pdcaclient.rest.EmployeeRestClient;
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

public class DeleteEmployeeController implements Initializable {

    @FXML
    private BorderPane deleteEmployeeBorderPane;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label employeeNameLabel;
    @FXML
    private Label employeeSurnameLabel;
    @FXML
    private Label employeeEmailLabel;

    private long employeeId;
    private final EmployeeRestClient employeeRestClient;
    private String token;
    private EmployeeListViewController employeeListViewController;

    public DeleteEmployeeController() {
        this.employeeRestClient = new EmployeeRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeDeleteButton();
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(deleteAction -> {
            Thread thread = new Thread(() -> {
                try {
                    employeeRestClient.deleteEmployee(getToken(), employeeId, () -> Platform.runLater(() -> {
                        getStage().close();
                        try {
                            this.employeeListViewController.refreshProjectList();
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

    private void initializeCancelButton() {
        cancelButton.setOnAction(cancelAction -> getStage().close());
    }

    private Stage getStage() {
        return (Stage) deleteEmployeeBorderPane.getScene().getWindow();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void loadEmployeeData(EmployeeTableModel selectedEmployee) {
        this.employeeId = selectedEmployee.getId();
        employeeNameLabel.setText(selectedEmployee.getName());
        employeeSurnameLabel.setText(selectedEmployee.getSurname());
        employeeEmailLabel.setText(selectedEmployee.getEmail());
    }

    public void setController(EmployeeListViewController employeeListViewController) {
        this.employeeListViewController = employeeListViewController;
    }
}
