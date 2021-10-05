package io.github.danielzyla.pdcaclient.controller;


import io.github.danielzyla.pdcaclient.dto.CheckPhaseReadDto;
import io.github.danielzyla.pdcaclient.dto.CheckPhaseWriteDto;
import io.github.danielzyla.pdcaclient.rest.CheckPhaseRestClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class EditCheckPhaseController implements Initializable {

    @FXML
    private BorderPane editCheckPhaseBorderPane;
    @FXML
    private TitledPane checkPhaseTitlePane;
    @FXML
    private Label checkStartTimeLabel;
    @FXML
    private CheckBox statusCheckBox;
    @FXML
    private Button saveCheckPhaseButton;
    @FXML
    private Button cancelEditCheckPhaseButton;
    @FXML
    private TextArea checkResultsTextArea;
    @FXML
    private TextArea checkTargetTextArea;
    @FXML
    private TextArea checkIfImproveTextArea;

    private final CheckPhaseRestClient checkPhaseRestClient;
    private final String token;
    private final CheckPhaseReadDto checkPhaseReadDto;
    private final CycleViewController cycleViewController;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EditCheckPhaseController(String token, CheckPhaseReadDto checkPhaseReadDto, CycleViewController cycleViewController) {
        this.checkPhaseRestClient = new CheckPhaseRestClient();
        this.token = token;
        this.checkPhaseReadDto = checkPhaseReadDto;
        this.cycleViewController = cycleViewController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCheckPhaseData();
        initializeSaveButton();
        initializeCancelButton();
    }

    private void loadCheckPhaseData() {
        if (checkPhaseReadDto.getStartTime() != null) {
            checkStartTimeLabel.setText(dateTimeFormatter.format(checkPhaseReadDto.getStartTime()));
        }
        statusCheckBox.setSelected(checkPhaseReadDto.isComplete());
        if (checkPhaseReadDto.getConclusions() != null) {
            checkResultsTextArea.setText(checkPhaseReadDto.getConclusions());
        }
        if (checkPhaseReadDto.getAchievements() != null) {
            checkTargetTextArea.setText(checkPhaseReadDto.getAchievements());
        }
        if (checkPhaseReadDto.getNextSteps() != null) {
            checkIfImproveTextArea.setText(checkPhaseReadDto.getNextSteps());
        }
    }

    private void initializeSaveButton() {
        saveCheckPhaseButton.setOnAction(saveCheckPhaseAction -> {
            CheckPhaseWriteDto checkPhaseWriteDto = new CheckPhaseWriteDto();
            checkPhaseWriteDto.setId(checkPhaseReadDto.getId());
            checkPhaseWriteDto.setConclusions(checkResultsTextArea.getText());
            checkPhaseWriteDto.setAchievements(checkTargetTextArea.getText());
            checkPhaseWriteDto.setNextSteps(checkIfImproveTextArea.getText());
            checkPhaseWriteDto.setComplete(statusCheckBox.isSelected());
            Thread thread = new Thread(() -> {
                try {
                    checkPhaseRestClient.updateCheckPhase(this.token, checkPhaseWriteDto, () -> Platform.runLater(() -> {
                        getStage().close();
                        try {
                            cycleViewController.setCheckPhaseReadDto(null);
                            cycleViewController.setActPhaseReadDto(null);
                            cycleViewController.initializeCheckPhaseViewData();
                            cycleViewController.initializeActPhaseViewData();
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

    private Stage getStage() {
        return (Stage) editCheckPhaseBorderPane.getScene().getWindow();
    }

    private void initializeCancelButton() {
        cancelEditCheckPhaseButton.setOnAction(exitStageAction -> getStage().close());
    }
}
