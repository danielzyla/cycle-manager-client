package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.ActPhaseReadDto;
import io.github.danielzyla.pdcaclient.dto.ActPhaseWriteApiDto;
import io.github.danielzyla.pdcaclient.model.TaskTableModel;
import io.github.danielzyla.pdcaclient.rest.ActPhaseRestClient;
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

public class EditActPhaseController implements Initializable {

    @FXML
    private BorderPane editActPhaseBorderPane;
    @FXML
    private TitledPane actPhaseTitlePane;
    @FXML
    private Label actStartTimeLabel;
    @FXML
    private CheckBox statusCheckBox;
    @FXML
    private CheckBox nextCycleCheckBox;
    @FXML
    private Button saveActPhaseButton;
    @FXML
    private Button cancelEditActPhaseButton;
    @FXML
    private TextArea actAssessmentTextArea;
    @FXML
    private TableView<TaskTableModel> actPhaseTaskTableView;

    private final ActPhaseRestClient actPhaseRestClient;
    private final String token;
    private final ActPhaseReadDto actPhaseReadDto;
    private final CycleViewController cycleViewController;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ShowCycleListController showCycleListController;

    public EditActPhaseController(String token, ActPhaseReadDto actPhaseReadDto, CycleViewController cycleViewController, ShowCycleListController showCycleListController) {
        this.actPhaseRestClient = new ActPhaseRestClient();
        this.token = token;
        this.actPhaseReadDto = actPhaseReadDto;
        this.cycleViewController = cycleViewController;
        this.showCycleListController = showCycleListController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadActPhaseData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initializeSaveButton();
        initializeCancelButton();
    }

    private void loadActPhaseData() throws InterruptedException {
        if (actPhaseReadDto.getStartTime() != null) {
            actStartTimeLabel.setText(dateTimeFormatter.format(actPhaseReadDto.getStartTime()));
        }
        statusCheckBox.setSelected(actPhaseReadDto.isComplete());
        nextCycleCheckBox.setSelected(actPhaseReadDto.isNextCycle());
        if (actPhaseReadDto.getDescription() != null) {
            actAssessmentTextArea.setText(actPhaseReadDto.getDescription());
        }

        initializeDoPhaseTaskTableView();
    }

    private void initializeSaveButton() {
        saveActPhaseButton.setOnAction(saveChangesAction -> {
            ActPhaseWriteApiDto actPhaseWriteApiDto = new ActPhaseWriteApiDto();
            actPhaseWriteApiDto.setId(actPhaseReadDto.getId());
            actPhaseWriteApiDto.setDescription(actAssessmentTextArea.getText());
            actPhaseWriteApiDto.setComplete(statusCheckBox.isSelected());
            actPhaseWriteApiDto.setNextCycle(nextCycleCheckBox.isSelected());
            Thread thread = new Thread(() -> {
                try {
                    actPhaseRestClient.updateActPhase(this.token, actPhaseWriteApiDto, () -> Platform.runLater(() -> {
                        getStage().close();
                        cycleViewController.setActPhaseReadDto(null);
                        try {
                            cycleViewController.clearActPhaseTaskTableView();
                            cycleViewController.initializeActPhaseViewData();
                            showCycleListController.initializeNextCycleButton();
                            showCycleListController.initializeCycleListView();
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
        actPhaseTaskTableView.getColumns().addAll(
                cycleViewController.setStartTimeTableColumn(),
                cycleViewController.setDescriptionTableColumn(),
                cycleViewController.setEmployeeListTableColumn(),
                cycleViewController.setDeadlineTableColumn(),
                cycleViewController.setTaskStatusTableColumn(),
                cycleViewController.setCompleteTableColumn(),
                cycleViewController.setExecutionTimeTableColumn()
        );
        ObservableList<TaskTableModel> data = FXCollections.observableArrayList();
        loadActPhaseTaskData(data);
        actPhaseTaskTableView.setItems(data);
    }

    private void loadActPhaseTaskData(ObservableList<TaskTableModel> data) throws InterruptedException {
        Thread thread = new Thread(() -> data.addAll(actPhaseReadDto.getActPhaseTasks().stream()
                .map(TaskTableModel::new)
                .collect(Collectors.toList())));
        thread.setDaemon(true);
        thread.start();
        thread.join();
    }

    private Stage getStage() {
        return (Stage) editActPhaseBorderPane.getScene().getWindow();
    }

    private void initializeCancelButton() {
        cancelEditActPhaseButton.setOnAction(exitStageAction -> getStage().close());
    }
}
