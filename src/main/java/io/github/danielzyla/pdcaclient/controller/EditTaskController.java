package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.EmployeeReadDto;
import io.github.danielzyla.pdcaclient.dto.TaskWriteDto;
import io.github.danielzyla.pdcaclient.model.Employee;
import io.github.danielzyla.pdcaclient.model.TaskStatus;
import io.github.danielzyla.pdcaclient.model.TaskTableModel;
import io.github.danielzyla.pdcaclient.rest.EmployeeRestClient;
import io.github.danielzyla.pdcaclient.rest.TaskRestClient;
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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EditTaskController implements Initializable {

    @FXML
    private BorderPane editTaskBorderPane;
    @FXML
    private Button saveTaskButton;
    @FXML
    private Button cancelEditTaskButton;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private ListView<EmployeeReadDto> employeeListView;
    @FXML
    private DatePicker deadlineDatePicker;
    @FXML
    private ComboBox<String> taskStatusComboBox;

    private final String token;
    private final TaskTableModel selectedTask;
    private final CycleViewController cycleViewController;
    private final EmployeeRestClient employeeRestClient;
    private final TaskRestClient taskRestClient;

    public EditTaskController(String token, TaskTableModel selectedTask, CycleViewController cycleViewController) {
        this.token = token;
        this.selectedTask = selectedTask;
        this.cycleViewController = cycleViewController;
        this.employeeRestClient = new EmployeeRestClient();
        this.taskRestClient = new TaskRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDataToEditTaskStage();
        initializeSaveButton();
        initializeCancelButton();
    }

    private void loadDataToEditTaskStage() {
        if (selectedTask.getDescription() != null) {
            descriptionTextArea.setText(selectedTask.getDescription());
        }
        if (selectedTask.getDeadline() != null) {
            deadlineDatePicker.setValue(selectedTask.getDeadline());
        }
        loadEmployeeListView();
        ObservableList<String> taskStatuses = FXCollections.observableArrayList(Arrays.stream(TaskStatus.values()).map(TaskStatus::getEngStatusName).collect(Collectors.toList()));
        taskStatusComboBox.setItems(taskStatuses);
        taskStatusComboBox.getSelectionModel().select(TaskStatus.valueOf(selectedTask.getTaskStatus()).getEngStatusName());
    }

    private void loadEmployeeListView() {
        Thread thread = new Thread(() -> {
            try {
                List<EmployeeReadDto> employeeReadDtoList = employeeRestClient.getEmployees(this.token);
                Platform.runLater(() -> {
                    employeeListView.setItems(FXCollections.observableArrayList(employeeReadDtoList));
                    employeeListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                    ObservableList<EmployeeReadDto> items = employeeListView.getItems();
                    for (EmployeeReadDto item : items) {
                        for (Employee e : selectedTask.getEmployees()) {
                            if (e.getId() == item.getId()) {
                                employeeListView.getSelectionModel().select(item);
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

    private void initializeSaveButton() {
        saveTaskButton.setOnAction(saveTaskAction -> {
            var task = new TaskWriteDto();
            task.setId(selectedTask.getId());
            task.setDescription(descriptionTextArea.getText());
            task.setEmployeeIds(getEmployeeIds());
            task.setDeadline(deadlineDatePicker.getValue());
            task.setTaskStatus(convertTaskStatus(taskStatusComboBox.getSelectionModel().getSelectedItem()));
            Thread thread = new Thread(() -> {
                try {
                    taskRestClient.updateTask(this.token, task, () -> Platform.runLater(() -> {
                        getStage().close();
                        if (selectedTask.getTaskClass().equals("DoPhaseTask")) {
                            cycleViewController.setDoPhaseReadDto(null);
                            cycleViewController.clearDoPhaseTaskTableView();
                            try {
                                cycleViewController.initializeDoPhaseViewData();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (selectedTask.getTaskClass().equals("ActPhaseTask")) {
                            cycleViewController.setActPhaseReadDto(null);
                            cycleViewController.clearActPhaseTaskTableView();
                            try {
                                cycleViewController.initializeActPhaseViewData();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }));
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
        });
    }

    private TaskStatus convertTaskStatus(String engStatusName) {
        switch (engStatusName) {
            case "AWAITING":
                return TaskStatus.OCZEKUJE;
            case "PENDING":
                return TaskStatus.W_REALIZACJI;
            case "DONE":
                return TaskStatus.WYKONANE;
            default:
                throw new IllegalStateException("status error");
        }
    }

    private List<Long> getEmployeeIds() {
        ObservableList<EmployeeReadDto> selectedItems = employeeListView.getSelectionModel().getSelectedItems();
        List<Long> selectedEmployees = new ArrayList<>();
        for (EmployeeReadDto dto : selectedItems) {
            selectedEmployees.add(dto.getId());
        }
        return selectedEmployees;
    }

    private Stage getStage() {
        return (Stage) editTaskBorderPane.getScene().getWindow();
    }

    private void initializeCancelButton() {
        cancelEditTaskButton.setOnAction(exitStageAction -> getStage().close());
    }
}
