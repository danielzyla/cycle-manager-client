package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.DepartmentReadDto;
import io.github.danielzyla.pdcaclient.dto.EmployeeWriteApiDto;
import io.github.danielzyla.pdcaclient.model.EmployeeTableModel;
import io.github.danielzyla.pdcaclient.rest.DepartmentRestClient;
import io.github.danielzyla.pdcaclient.rest.EmployeeRestClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditEmployeeController implements Initializable {

    @FXML
    private BorderPane editEmployeeBorderPane;
    @FXML
    private TextField employeeNameTextField;
    @FXML
    private TextField employeeSurnameTextField;
    @FXML
    private TextField employeeEmailTextField;
    @FXML
    private ComboBox<DepartmentReadDto> departmentListComboBox;
    @FXML
    private Button editButton;
    @FXML
    private Button cancelButton;

    private final EmployeeRestClient employeeRestClient;
    private final DepartmentRestClient departmentRestClient;
    private final String token;
    private final EmployeeTableModel selectedEmployee;
    private final EmployeeListViewController employeeListViewController;
    private final static Pattern PATTERN = Pattern.compile("^[\\p{L}0-9\\s]+$");

    public EditEmployeeController(
            String token,
            EmployeeTableModel selectedEmployee,
            EmployeeListViewController employeeListViewController) {
        this.employeeRestClient = new EmployeeRestClient();
        this.departmentRestClient = new DepartmentRestClient();
        this.token = token;
        this.selectedEmployee = selectedEmployee;
        this.employeeListViewController = employeeListViewController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEmployeeWriteApiDto();
        loadDepartmentList();
        initializeEditButton();
        initializeCancelButton();
    }

    private void initializeEditButton() {
        editButton.setOnAction(editAction -> {
            if (validateName() && validateSurname() && validateEmail() && validateDepartment()) {
                EmployeeWriteApiDto employeeWriteApiDto = new EmployeeWriteApiDto();
                employeeWriteApiDto.setId(this.selectedEmployee.getId());
                employeeWriteApiDto.setName(employeeNameTextField.getText());
                employeeWriteApiDto.setSurname(employeeSurnameTextField.getText());
                employeeWriteApiDto.setEmail(employeeEmailTextField.getText());
                employeeWriteApiDto.setDepartmentId(departmentListComboBox.getSelectionModel().getSelectedItem().getId());
                Thread thread = new Thread(() -> {
                    try {
                        employeeRestClient.updateEmployee(
                                this.token,
                                employeeWriteApiDto,
                                () -> Platform.runLater(() -> {
                                    getStage().close();
                                    try {
                                        this.employeeListViewController.refreshProjectList();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                })
                        );
                    } catch (IOException | InterruptedException e) {
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
        });
    }

    private boolean validateName() {
        Matcher matcher = PATTERN.matcher(employeeNameTextField.getText());
        if (
                matcher.find()
                        && matcher.group().equals(employeeNameTextField.getText())
                        && !employeeNameTextField.getText().isEmpty()
        ) {
            return true;
        } else {
            validationAlert("employee name");
            return false;
        }
    }

    private boolean validateSurname() {
        Matcher matcher = PATTERN.matcher(employeeSurnameTextField.getText());
        if (matcher.find() && matcher.group().equals(employeeSurnameTextField.getText())) {
            return true;
        } else {
            validationAlert("employee surname");
            return false;
        }
    }

    private boolean validateDepartment() {
        if (departmentListComboBox.getSelectionModel().getSelectedItem() != null) {
            return true;
        } else {
            validationAlert("department");
            return false;
        }
    }

    private boolean validateEmail() {
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(employeeEmailTextField.getText());
        if (matcher.find() && matcher.group().equals(employeeEmailTextField.getText())) {
            return true;
        } else {
            validationAlert("email address");
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

    private void loadDepartmentList() {
        Thread thread = new Thread(() -> {
            try {
                List<DepartmentReadDto> departmentReadDtoList = departmentRestClient.getDepartments(this.token);
                Platform.runLater(() -> {
                    departmentListComboBox.setItems(FXCollections.observableArrayList(departmentReadDtoList));
                    ObservableList<DepartmentReadDto> items = departmentListComboBox.getItems();
                    if (selectedEmployee.getDepartment() != null) {
                        for (DepartmentReadDto item : items) {
                            if (selectedEmployee.getDepartment().getId() == item.getId()) {
                                departmentListComboBox.getSelectionModel().select(item);
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

    public void loadEmployeeWriteApiDto() {
        employeeNameTextField.setText(selectedEmployee.getName());
        employeeSurnameTextField.setText(selectedEmployee.getSurname());
        employeeEmailTextField.setText(selectedEmployee.getEmail());
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(cancelAction -> getStage().close());
    }

    private Stage getStage() {
        return (Stage) editEmployeeBorderPane.getScene().getWindow();
    }
}
