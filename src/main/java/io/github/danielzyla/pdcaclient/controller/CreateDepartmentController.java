package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.DepartmentWriteDto;
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

public class CreateDepartmentController implements Initializable {

    @FXML
    private BorderPane createDepartmentBorderPane;
    @FXML
    private TextField departmentNameTextField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private final DepartmentRestClient departmentRestClient;
    private DepartmentListViewController departmentListViewController;
    private String token;
    private final static Pattern PATTERN = Pattern.compile("\\A(?!\\s*\\Z).+");

    public CreateDepartmentController() {
        this.departmentRestClient = new DepartmentRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeSaveButton();
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(saveDepartmentAction -> {
            if (validateDepartmentName()) {
                DepartmentWriteDto departmentWriteDto = new DepartmentWriteDto();
                departmentWriteDto.setDeptName(departmentNameTextField.getText());
                Thread thread = new Thread(() -> {
                    try {
                        departmentRestClient.saveDepartment(getToken(), departmentWriteDto, () -> Platform.runLater(() -> {
                            getStage().close();
                            try {
                                departmentListViewController.refreshProductList();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }));
                    } catch (IOException | InterruptedException e) {
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

    private void initializeCancelButton() {
        cancelButton.setOnAction(cancelAction -> getStage().close());
    }

    private Stage getStage() {
        return (Stage) createDepartmentBorderPane.getScene().getWindow();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setController(DepartmentListViewController departmentListViewController) {
        this.departmentListViewController = departmentListViewController;
    }
}
