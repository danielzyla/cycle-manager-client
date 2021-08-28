package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.DepartmentWriteDto;
import io.github.danielzyla.pdcaclient.model.DepartmentTableModel;
import io.github.danielzyla.pdcaclient.rest.DepartmentRestClient;
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

public class EditDepartmentController implements Initializable {

    @FXML
    private BorderPane editDepartmentBorderPane;
    @FXML
    private TextField departmentNameTextField;
    @FXML
    private Button editButton;
    @FXML
    private Button cancelButton;

    private final DepartmentRestClient departmentRestClient;
    private String token;
    private DepartmentTableModel selectedDepartment;
    private DepartmentListViewController departmentListViewController;
    private final static Pattern PATTERN = Pattern.compile("\\A(?!\\s*\\Z).+");

    public EditDepartmentController() {
        this.departmentRestClient = new DepartmentRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeEditButton();
        initializeCancelButton();
    }

    private void initializeEditButton() {
        editButton.setOnAction(editAction -> {
            if (validateDepartmentName()) {
                DepartmentWriteDto departmentWriteDto = new DepartmentWriteDto();
                departmentWriteDto.setId(selectedDepartment.getId());
                departmentWriteDto.setDeptName(departmentNameTextField.getText());
                Thread thread = new Thread(() -> {
                    try {
                        departmentRestClient.updateDepartment(
                                getToken(),
                                departmentWriteDto,
                                () -> Platform.runLater(() -> {
                                    getStage().close();
                                    try {
                                        this.departmentListViewController.refreshProductList();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                })
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.setDaemon(true);
                thread.start();
            }
        });
    }

    private boolean validateDepartmentName() {
        Matcher matcher = PATTERN.matcher(departmentNameTextField.getText());
        if (matcher.find() && matcher.group().equals(departmentNameTextField.getText())) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate department name");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid department name");
            alert.showAndWait();
            return false;
        }
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void loadDepartmentWriteDto(DepartmentTableModel selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
        departmentNameTextField.setText(selectedDepartment.getDeptName());
    }

    public void setController(DepartmentListViewController departmentListViewController) {
        this.departmentListViewController = departmentListViewController;
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(cancelAction -> getStage().close());
    }

    private Stage getStage() {
        return (Stage) editDepartmentBorderPane.getScene().getWindow();
    }
}
