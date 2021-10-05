package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.DoPhaseReadDto;
import io.github.danielzyla.pdcaclient.dto.DoPhaseWriteApiDto;
import io.github.danielzyla.pdcaclient.model.TaskTableModel;
import io.github.danielzyla.pdcaclient.rest.DoPhaseRestClient;
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
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EditDoPhaseController implements Initializable {

    @FXML
    private BorderPane editDoPhaseBorderPane;
    @FXML
    private TitledPane doPhaseTitlePane;
    @FXML
    private Label doStartTimeLabel;
    @FXML
    private CheckBox doPhaseStatusCheckBox;
    @FXML
    private Button saveDoPhaseButton;
    @FXML
    private Button cancelEditDoPhaseButton;
    @FXML
    private TextArea doPreparationTextArea;
    @FXML
    private TableView<TaskTableModel> doPhaseTaskTableView;

    private final DoPhaseRestClient doPhaseRestClient;
    private final String token;
    private final DoPhaseReadDto doPhaseReadDto;
    private final CycleViewController cycleViewController;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EditDoPhaseController(String token, DoPhaseReadDto doPhaseReadDto, CycleViewController cycleViewController) {
        this.doPhaseRestClient = new DoPhaseRestClient();
        this.token = token;
        this.doPhaseReadDto = doPhaseReadDto;
        this.cycleViewController = cycleViewController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadDoPhaseData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initializeSaveButton();
        initializeCancelButton();
    }

    private void loadDoPhaseData() throws InterruptedException {
        if (doPhaseReadDto.getStartTime() != null) {
            doStartTimeLabel.setText(dateTimeFormatter.format(doPhaseReadDto.getStartTime()));
        }
        doPhaseStatusCheckBox.setSelected(doPhaseReadDto.isComplete());
        if (doPhaseReadDto.getDescription() != null) {
            doPreparationTextArea.setText(doPhaseReadDto.getDescription());
        }

        initializeDoPhaseTaskTableView();
    }

    private void initializeSaveButton() {
        saveDoPhaseButton.setOnAction(saveChangesAction -> {
            DoPhaseWriteApiDto doPhaseWriteApiDto = new DoPhaseWriteApiDto();
            doPhaseWriteApiDto.setId(doPhaseReadDto.getId());
            doPhaseWriteApiDto.setDescription(doPreparationTextArea.getText());
            doPhaseWriteApiDto.setComplete(doPhaseStatusCheckBox.isSelected());
            Thread thread = new Thread(() -> {
                try {
                    doPhaseRestClient.updateDoPhase(this.token, doPhaseWriteApiDto, () -> Platform.runLater(() -> {
                        getStage().close();
                        cycleViewController.setDoPhaseReadDto(null);
                        cycleViewController.setCheckPhaseReadDto(null);
                        try {
                            cycleViewController.clearDoPhaseTaskTableView();
                            cycleViewController.initializeDoPhaseViewData();
                            cycleViewController.initializeCheckPhaseViewData();
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
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings(value = "unchecked")
    private void initializeDoPhaseTaskTableView() throws InterruptedException {
        doPhaseTaskTableView.getColumns().addAll(
                cycleViewController.setStartTimeTableColumn(),
                cycleViewController.setDescriptionTableColumn(),
                cycleViewController.setEmployeeListTableColumn(),
                cycleViewController.setDeadlineTableColumn(),
                cycleViewController.setTaskStatusTableColumn(),
                cycleViewController.setCompleteTableColumn(),
                cycleViewController.setExecutionTimeTableColumn()
        );
        ObservableList<TaskTableModel> data = FXCollections.observableArrayList();
        loadDoPhaseTaskData(data);
        doPhaseTaskTableView.setItems(data);
    }

    private void loadDoPhaseTaskData(ObservableList<TaskTableModel> data) throws InterruptedException {
        Thread thread = new Thread(() -> data.addAll(doPhaseReadDto.getDoPhaseTasks().stream()
                .map(TaskTableModel::new)
                .collect(Collectors.toList())));
        thread.setDaemon(true);
        thread.start();
        thread.join();
    }

    private Stage getStage() {
        return (Stage) editDoPhaseBorderPane.getScene().getWindow();
    }

    private void initializeCancelButton() {
        cancelEditDoPhaseButton.setOnAction(exitStageAction -> getStage().close());
    }
}
