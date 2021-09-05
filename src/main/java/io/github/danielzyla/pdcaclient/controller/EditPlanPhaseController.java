package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.EmployeeReadDto;
import io.github.danielzyla.pdcaclient.dto.PlanPhaseReadDto;
import io.github.danielzyla.pdcaclient.dto.PlanPhaseWriteApiDto;
import io.github.danielzyla.pdcaclient.model.Employee;
import io.github.danielzyla.pdcaclient.rest.EmployeeRestClient;
import io.github.danielzyla.pdcaclient.rest.PlanPhaseRestClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditPlanPhaseController implements Initializable {

    @FXML
    private BorderPane editPlanPhaseBorderPane;
    @FXML
    private TitledPane planPhaseTitlePane;
    @FXML
    private Label planStartTimeLabel;
    @FXML
    private CheckBox planStatusCheckBox;
    @FXML
    private Button savePlanPhaseButton;
    @FXML
    private Button cancelEditPlanPhaseButton;
    @FXML
    private ListView<EmployeeReadDto> employeeListView;
    @FXML
    private TextArea problemTextArea;
    @FXML
    private TextArea currentSituationTextArea;
    @FXML
    private TextArea goalTextArea;
    @FXML
    private TextArea rootCauseTextArea;
    @FXML
    private TextArea optimalSolutionTextArea;

    private final String token;
    private final PlanPhaseReadDto planPhaseReadDto;
    private final CycleViewController cycleViewController;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final PlanPhaseRestClient planPhaseRestClient;
    private final EmployeeRestClient employeeRestClient;

    public EditPlanPhaseController(String token, PlanPhaseReadDto planPhaseReadDto, CycleViewController cycleViewController) {
        this.token = token;
        this.planPhaseReadDto = planPhaseReadDto;
        this.cycleViewController = cycleViewController;
        this.planPhaseRestClient = new PlanPhaseRestClient();
        this.employeeRestClient = new EmployeeRestClient();
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPlanPhaseData();
        loadEmployeeListView();
        initializeSaveButton();
        initializeCancelButton();
    }

    private void loadPlanPhaseData() {
        if (planPhaseReadDto.getStartTime() != null) {
            planStartTimeLabel.setText(dateTimeFormatter.format(planPhaseReadDto.getStartTime()));
        }
        planStatusCheckBox.setSelected(planPhaseReadDto.isComplete());
        if (planPhaseReadDto.getProblemDescription() != null) {
            problemTextArea.setText(planPhaseReadDto.getProblemDescription());
        }
        if (planPhaseReadDto.getCurrentSituationAnalysis() != null) {
            currentSituationTextArea.setText(planPhaseReadDto.getCurrentSituationAnalysis());
        }
        if (planPhaseReadDto.getGoal() != null) {
            goalTextArea.setText(planPhaseReadDto.getGoal());
        }
        if (planPhaseReadDto.getRootCauseIdentification() != null) {
            rootCauseTextArea.setText(planPhaseReadDto.getRootCauseIdentification());
        }
        if (planPhaseReadDto.getOptimalSolutionChoice() != null) {
            optimalSolutionTextArea.setText(planPhaseReadDto.getOptimalSolutionChoice());
        }
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
                        for (Employee e : planPhaseReadDto.getEmployees()) {
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
        savePlanPhaseButton.setOnAction(saveChangesAction -> {
            PlanPhaseWriteApiDto planPhaseWriteApiDto = new PlanPhaseWriteApiDto();
            planPhaseWriteApiDto.setId(planPhaseReadDto.getId());
            planPhaseWriteApiDto.setProblemDescription(problemTextArea.getText());
            planPhaseWriteApiDto.setEmployeeIds(getEmployeeIds());
            planPhaseWriteApiDto.setCurrentSituationAnalysis(currentSituationTextArea.getText());
            planPhaseWriteApiDto.setGoal(goalTextArea.getText());
            planPhaseWriteApiDto.setRootCauseIdentification(rootCauseTextArea.getText());
            planPhaseWriteApiDto.setOptimalSolutionChoice(optimalSolutionTextArea.getText());
            planPhaseWriteApiDto.setComplete(planStatusCheckBox.isSelected());
            Thread thread = new Thread(() -> {
                try {
                    planPhaseRestClient.updatePlanPhase(this.token, planPhaseWriteApiDto, () -> Platform.runLater(() -> {
                        getStage().close();
                        cycleViewController.setPlanPhaseReadDto(null);
                        cycleViewController.setDoPhaseReadDto(null);
                        try {
                            cycleViewController.initializePlanPhaseViewData();
                            cycleViewController.initializeDoPhaseViewData();
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

    private List<Long> getEmployeeIds() {
        ObservableList<EmployeeReadDto> selectedItems = employeeListView.getSelectionModel().getSelectedItems();
        List<Long> selectedEmployees = new ArrayList<>();
        for (EmployeeReadDto dto : selectedItems) {
            selectedEmployees.add(dto.getId());
        }
        return selectedEmployees;
    }

    private Stage getStage() {
        return (Stage) editPlanPhaseBorderPane.getScene().getWindow();
    }

    private void initializeCancelButton() {
        cancelEditPlanPhaseButton.setOnAction(exitStageAction -> getStage().close());
    }
}
