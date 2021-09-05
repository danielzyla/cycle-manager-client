package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.model.TaskTableModel;
import io.github.danielzyla.pdcaclient.rest.TaskRestClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteTaskController implements Initializable {

    @FXML
    private BorderPane deleteTaskBorderPane;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label descriptionLabel;

    private final String token;
    private final TaskTableModel selectedTask;
    private final CycleViewController cycleViewController;
    private final TaskRestClient taskRestClient;

    public DeleteTaskController(String token, TaskTableModel selectedTask, CycleViewController cycleViewController) {
        this.token = token;
        this.selectedTask = selectedTask;
        this.cycleViewController = cycleViewController;
        this.taskRestClient = new TaskRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTaskData();
        initializeCancelButton();
        initializeDeleteButton();
    }

    private void loadTaskData() {
        descriptionLabel.setText(selectedTask.getDescription());
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(deleteTaskAction -> {
            Thread thread = new Thread(() -> taskRestClient.deleteTask(
                    this.token,
                    selectedTask.getId(),
                    () -> Platform.runLater(() -> {
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
                    })));
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
        return (Stage) deleteTaskBorderPane.getScene().getWindow();
    }
}
