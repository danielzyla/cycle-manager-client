package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.ActPhaseReadDto;
import io.github.danielzyla.pdcaclient.dto.CheckPhaseReadDto;
import io.github.danielzyla.pdcaclient.dto.DoPhaseReadDto;
import io.github.danielzyla.pdcaclient.dto.PlanPhaseReadDto;
import io.github.danielzyla.pdcaclient.model.Cycle;
import io.github.danielzyla.pdcaclient.model.Employee;
import io.github.danielzyla.pdcaclient.model.TaskStatus;
import io.github.danielzyla.pdcaclient.model.TaskTableModel;
import io.github.danielzyla.pdcaclient.rest.ActPhaseRestClient;
import io.github.danielzyla.pdcaclient.rest.CheckPhaseRestClient;
import io.github.danielzyla.pdcaclient.rest.DoPhaseRestClient;
import io.github.danielzyla.pdcaclient.rest.PlanPhaseRestClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CycleViewController implements Initializable {

    @FXML
    private BorderPane cycleViewBorderPane;
    @FXML
    private Label startTimeLabel;
    @FXML
    private Label endTimeLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private TitledPane planPhaseTitlePane;
    @FXML
    private Label planStartTimeLabel;
    @FXML
    private Label planStatusLabel;
    @FXML
    private Label planEndTimeLabel;
    @FXML
    private Button editPlanPhaseButton;
    @FXML
    private TableView<TaskTableModel> doPhaseTaskTableView;
    @FXML
    private TextArea teamTextArea;
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
    @FXML
    private TitledPane doPhaseTitlePane;
    @FXML
    private Label doStartTimeLabel;
    @FXML
    private Label doStatusLabel;
    @FXML
    private Label doEndTimeLabel;
    @FXML
    private Button editDoPhaseButton;
    @FXML
    private TextArea doPreparationTextArea;
    @FXML
    private Button newDoPhaseTaskButton;
    @FXML
    private Button editDoPhaseTaskButton;
    @FXML
    private Button deleteDoPhaseTaskButton;
    @FXML
    private TitledPane checkPhaseTitlePane;
    @FXML
    private Label checkStartTimeLabel;
    @FXML
    private Label checkStatusLabel;
    @FXML
    private Label checkEndTimeLabel;
    @FXML
    private Button editCheckPhaseButton;
    @FXML
    private TextArea checkResultsTextArea;
    @FXML
    private TextArea checkTargetTextArea;
    @FXML
    private TextArea checkIfImproveTextArea;
    @FXML
    private TitledPane actPhaseTitlePane;
    @FXML
    private Label actStartTimeLabel;
    @FXML
    private Label actStatusLabel;
    @FXML
    private Label actEndTimeLabel;
    @FXML
    private Button editActPhaseButton;
    @FXML
    private TextArea actAssessmentTextArea;
    @FXML
    private Label actNextCycleStatusLabel;
    @FXML
    private Button newActPhaseTaskButton;
    @FXML
    private Button editActPhaseTaskButton;
    @FXML
    private Button deleteActPhaseTaskButton;
    @FXML
    private TableView<TaskTableModel> actPhaseTaskTableView;

    private final Cycle selectedCycle;
    private final String token;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final PlanPhaseRestClient planPhaseRestClient;
    private final DoPhaseRestClient doPhaseRestClient;
    private final CheckPhaseRestClient checkPhaseRestClient;
    private final ActPhaseRestClient actPhaseRestClient;
    private final ShowCycleListController showCycleListController;
    private PlanPhaseReadDto planPhaseReadDto;
    private DoPhaseReadDto doPhaseReadDto;
    private CheckPhaseReadDto checkPhaseReadDto;
    private ActPhaseReadDto actPhaseReadDto;

    public CycleViewController(Cycle selectedCycle, String token, ShowCycleListController showCycleListController) {
        this.selectedCycle = selectedCycle;
        this.token = token;
        this.planPhaseRestClient = new PlanPhaseRestClient();
        this.doPhaseRestClient = new DoPhaseRestClient();
        this.checkPhaseRestClient = new CheckPhaseRestClient();
        this.actPhaseRestClient = new ActPhaseRestClient();
        this.showCycleListController = showCycleListController;
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCycleStatusData();
        initializePlanPhaseViewData();
        initializeDoPhaseViewData();
        initializeCheckPhaseViewData();
        initializeActPhaseViewData();
    }

    void initializeCycleStatusData() {
        if (selectedCycle.getStartTime() != null) {
            startTimeLabel.setText(dateTimeFormatter.format(selectedCycle.getStartTime()));
        }
        if (selectedCycle.getEndTime() != null) {
            endTimeLabel.setText(dateTimeFormatter.format(selectedCycle.getEndTime()));
        }
        if (selectedCycle.isComplete()) {
            statusLabel.setText("completed");
        } else {
            statusLabel.setText("pending");
        }
    }

    void initializePlanPhaseViewData() throws InterruptedException {
        if (planPhaseReadDto == null) {
            Thread thread = new Thread(() -> {
                try {
                    planPhaseReadDto = planPhaseRestClient.getPlanPhaseById(this.token, selectedCycle.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.start();
            thread.join();

            if (planPhaseReadDto.getStartTime() != null) {
                planStartTimeLabel.setText(dateTimeFormatter.format(planPhaseReadDto.getStartTime()));
            }
            if (planPhaseReadDto.isComplete()) {
                planStatusLabel.setText("complete");
            } else {
                planStatusLabel.setText("pending");
            }
            if (planPhaseReadDto.getEndTime() != null) {
                planEndTimeLabel.setText(dateTimeFormatter.format(planPhaseReadDto.getEndTime()));
            }
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

            StringBuilder employees = new StringBuilder();
            for (Employee employee : planPhaseReadDto.getEmployees()) {
                employees.append(employee).append("\n");
            }
            teamTextArea.setText(employees.toString());
        }

        if (planPhaseReadDto.isComplete()) {
            editPlanPhaseButton.setVisible(false);
        }
        editPlanPhaseButton.setOnAction(editPlanPhaseAction -> initializeEditPlanPhaseStage());
    }

    private void initializeEditPlanPhaseStage() {
        if (planPhaseReadDto != null) {
            try {
                Stage editPlanPhaseStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/edit-plan-phase.fxml"
                ));
                EditPlanPhaseController controller = new EditPlanPhaseController(this.token, planPhaseReadDto, this);
                loader.setController(controller);
                Parent parent = loader.load();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(parent);
                Scene scene = new Scene(scrollPane, 1024, 1024);
                editPlanPhaseStage.setScene(scene);
                editPlanPhaseStage.initModality(Modality.APPLICATION_MODAL);
                editPlanPhaseStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void initializeDoPhaseViewData() throws InterruptedException {
        if (doPhaseReadDto == null) {
            Thread thread = new Thread(() -> {
                try {
                    doPhaseReadDto = doPhaseRestClient.getDoPhaseById(this.token, selectedCycle.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.start();
            thread.join();

            if (doPhaseReadDto.getStartTime() != null) {
                doStartTimeLabel.setText(dateTimeFormatter.format(doPhaseReadDto.getStartTime()));
            }
            if (doPhaseReadDto.isComplete()) {
                doStatusLabel.setText("complete");
            } else {
                doStatusLabel.setText("pending");
            }
            if (doPhaseReadDto.getEndTime() != null) {
                doEndTimeLabel.setText(dateTimeFormatter.format(doPhaseReadDto.getEndTime()));
            }
            if (doPhaseReadDto.getDescription() != null) {
                doPreparationTextArea.setText(doPhaseReadDto.getDescription());
            }

            initializeDoPhaseTaskTableView();

            if (!planPhaseReadDto.isComplete() || doPhaseReadDto.isComplete()) {
                editDoPhaseButton.setVisible(false);
                newDoPhaseTaskButton.setVisible(false);
                editDoPhaseTaskButton.setVisible(false);
                deleteDoPhaseTaskButton.setVisible(false);
            } else {
                editDoPhaseButton.setVisible(true);
                newDoPhaseTaskButton.setVisible(true);
                editDoPhaseTaskButton.setVisible(true);
                deleteDoPhaseTaskButton.setVisible(true);
            }
            editDoPhaseButton.setOnAction(editPlanPhaseAction -> initializeEditDoPhaseStage());
            newDoPhaseTaskButton.setOnAction(createNewDoPhaseTask -> {
                try {
                    createDoPhaseTask();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            editDoPhaseTaskButton.setOnAction(editDoPhaseTaskAction -> {
                TaskTableModel selectedTask = doPhaseTaskTableView.getSelectionModel().getSelectedItem();
                initializeEditTaskStage(selectedTask);
            });
            deleteDoPhaseTaskButton.setOnAction(deleteDoPhaseTaskAction -> {
                TaskTableModel selectedTask = doPhaseTaskTableView.getSelectionModel().getSelectedItem();
                initializeDeleteTaskStage(selectedTask);
            });
        }
    }

    private void createDoPhaseTask() throws InterruptedException {
        if (doPhaseReadDto != null) {
            Thread thread = new Thread(() -> {
                try {
                    doPhaseRestClient.createDoPhaseTask(this.token, doPhaseReadDto.getId(), () -> Platform.runLater(() -> {
                        clearDoPhaseTaskTableView();
                        try {
                            doPhaseReadDto = null;
                            initializeDoPhaseViewData();
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
            thread.join();
        }
    }

    private void initializeEditDoPhaseStage() {
        if (doPhaseReadDto != null) {
            try {
                Stage editDoPhaseStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/edit-do-phase.fxml"
                ));
                EditDoPhaseController controller = new EditDoPhaseController(this.token, doPhaseReadDto, this);
                loader.setController(controller);
                Parent parent = loader.load();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(parent);
                Scene scene = new Scene(scrollPane, 1024, 768);
                editDoPhaseStage.setScene(scene);
                editDoPhaseStage.initModality(Modality.APPLICATION_MODAL);
                editDoPhaseStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeDoPhaseTaskTableView() throws InterruptedException {
        doPhaseTaskTableView.getColumns().addAll(
                setStartTimeTableColumn(),
                setDescriptionTableColumn(),
                setEmployeeListTableColumn(),
                setDeadlineTableColumn(),
                setTaskStatusTableColumn(),
                setCompleteTableColumn(),
                setExecutionTimeTableColumn()
        );
        ObservableList<TaskTableModel> data = FXCollections.observableArrayList();
        loadDoPhaseTaskData(data);
        doPhaseTaskTableView.setItems(data);
    }

    void loadDoPhaseTaskData(ObservableList<TaskTableModel> data) throws InterruptedException {
        Thread thread = new Thread(() -> data.addAll(doPhaseReadDto.getDoPhaseTasks().stream()
                .map(TaskTableModel::new)
                .collect(Collectors.toList())));
        thread.setDaemon(true);
        thread.start();
        thread.join();
    }

    private void initializeEditTaskStage(TaskTableModel selectedTask) {
        if (selectedTask != null) {
            try {
                Stage editTaskStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/edit-task.fxml"
                ));
                EditTaskController controller = new EditTaskController(this.token, selectedTask, this);
                loader.setController(controller);
                Parent parent = loader.load();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(parent);
                Scene scene = new Scene(scrollPane, 600, 600);
                editTaskStage.setScene(scene);
                editTaskStage.initModality(Modality.APPLICATION_MODAL);
                editTaskStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeDeleteTaskStage(TaskTableModel selectedTask) {
        if (selectedTask != null) {
            try {
                Stage deleteTaskStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/delete-task.fxml"
                ));
                DeleteTaskController controller = new DeleteTaskController(this.token, selectedTask, this);
                loader.setController(controller);
                Parent parent = loader.load();
                Scene scene = new Scene(parent, 400, 250);
                deleteTaskStage.setScene(scene);
                deleteTaskStage.initModality(Modality.APPLICATION_MODAL);
                deleteTaskStage.initStyle(StageStyle.UNDECORATED);
                deleteTaskStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void initializeCheckPhaseViewData() throws InterruptedException {
        if (checkPhaseReadDto == null) {
            Thread thread = new Thread(() -> {
                try {
                    checkPhaseReadDto = checkPhaseRestClient.getCheckPhaseById(this.token, selectedCycle.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.start();
            thread.join();

            if (checkPhaseReadDto.getStartTime() != null) {
                checkStartTimeLabel.setText(dateTimeFormatter.format(checkPhaseReadDto.getStartTime()));
            }
            if (checkPhaseReadDto.isComplete()) {
                checkStatusLabel.setText("complete");
            } else {
                checkStatusLabel.setText("pending");
            }
            if (checkPhaseReadDto.getEndTime() != null) {
                checkEndTimeLabel.setText(dateTimeFormatter.format(checkPhaseReadDto.getEndTime()));
            }
            if (checkPhaseReadDto.getConclusions() != null) {
                checkResultsTextArea.setText(checkPhaseReadDto.getConclusions());
            }
            if (checkPhaseReadDto.getAchievements() != null) {
                checkTargetTextArea.setText(checkPhaseReadDto.getAchievements());
            }
            if (checkPhaseReadDto.getNextSteps() != null) {
                checkIfImproveTextArea.setText(checkPhaseReadDto.getNextSteps());
            }

            editCheckPhaseButton.setVisible(doPhaseReadDto.isComplete() && !checkPhaseReadDto.isComplete());
            editCheckPhaseButton.setOnAction(editCheckPhase -> initializeEditCheckPhaseStage());
        }
    }

    private void initializeEditCheckPhaseStage() {
        if (checkPhaseReadDto != null) {
            try {
                Stage checkDoPhaseStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/edit-check-phase.fxml"
                ));
                EditCheckPhaseController controller = new EditCheckPhaseController(this.token, checkPhaseReadDto, this);
                loader.setController(controller);
                Parent parent = loader.load();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(parent);
                Scene scene = new Scene(scrollPane, 1024, 768);
                checkDoPhaseStage.setScene(scene);
                checkDoPhaseStage.initModality(Modality.APPLICATION_MODAL);
                checkDoPhaseStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void initializeActPhaseViewData() throws InterruptedException {
        if (actPhaseReadDto == null) {
            Thread thread = new Thread(() -> {
                try {
                    actPhaseReadDto = actPhaseRestClient.getActPhaseById(this.token, selectedCycle.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.setDaemon(true);
            thread.start();
            thread.join();

            if (actPhaseReadDto.getStartTime() != null) {
                actStartTimeLabel.setText(dateTimeFormatter.format(actPhaseReadDto.getStartTime()));
            }
            if (actPhaseReadDto.isComplete()) {
                actStatusLabel.setText("complete");
            } else {
                actStatusLabel.setText("pending");
            }
            if (actPhaseReadDto.getEndTime() != null) {
                actEndTimeLabel.setText(dateTimeFormatter.format(actPhaseReadDto.getEndTime()));
            }
            if (actPhaseReadDto.isNextCycle()) {
                actNextCycleStatusLabel.setText("yes");
            } else {
                actNextCycleStatusLabel.setText("no");
            }
            if (actPhaseReadDto.getDescription() != null) {
                actAssessmentTextArea.setText(actPhaseReadDto.getDescription());
            }

            initializeActPhaseTaskTableView();

            if (!checkPhaseReadDto.isComplete() || actPhaseReadDto.isComplete()) {
                editActPhaseButton.setVisible(false);
                newActPhaseTaskButton.setVisible(false);
                editActPhaseTaskButton.setVisible(false);
                deleteActPhaseTaskButton.setVisible(false);
            } else {
                editActPhaseButton.setVisible(true);
                newActPhaseTaskButton.setVisible(true);
                editActPhaseTaskButton.setVisible(true);
                deleteActPhaseTaskButton.setVisible(true);
            }

            editActPhaseButton.setOnAction(editActPhaseAction -> initializeEditActPhaseStage());
            newActPhaseTaskButton.setOnAction(createNewActPhaseTask -> {
                try {
                    createActPhaseTask();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            editActPhaseTaskButton.setOnAction(editActPhaseTaskAction -> {
                TaskTableModel selectedTask = actPhaseTaskTableView.getSelectionModel().getSelectedItem();
                initializeEditTaskStage(selectedTask);
            });
            deleteActPhaseTaskButton.setOnAction(deleteActPhaseTaskAction -> {
                TaskTableModel selectedTask = actPhaseTaskTableView.getSelectionModel().getSelectedItem();
                initializeDeleteTaskStage(selectedTask);
            });
        }
    }

    private void createActPhaseTask() throws InterruptedException {
        if (actPhaseReadDto != null) {
            Thread thread = new Thread(() -> {
                try {
                    actPhaseRestClient.createActPhaseTask(this.token, actPhaseReadDto.getId(), () -> Platform.runLater(() -> {
                        clearActPhaseTaskTableView();
                        try {
                            actPhaseReadDto = null;
                            initializeActPhaseViewData();
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
            thread.join();
        }
    }

    private void initializeEditActPhaseStage() {
        if (actPhaseReadDto != null) {
            try {
                Stage actDoPhaseStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/edit-act-phase.fxml"
                ));
                EditActPhaseController controller = new EditActPhaseController(
                        this.token,
                        actPhaseReadDto,
                        this,
                        showCycleListController
                );
                loader.setController(controller);
                Parent parent = loader.load();
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(parent);
                Scene scene = new Scene(scrollPane, 1024, 768);
                actDoPhaseStage.setScene(scene);
                actDoPhaseStage.initModality(Modality.APPLICATION_MODAL);
                actDoPhaseStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeActPhaseTaskTableView() throws InterruptedException {
        actPhaseTaskTableView.getColumns().addAll(
                setStartTimeTableColumn(),
                setDescriptionTableColumn(),
                setEmployeeListTableColumn(),
                setDeadlineTableColumn(),
                setTaskStatusTableColumn(),
                setCompleteTableColumn(),
                setExecutionTimeTableColumn()
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

    TableColumn<TaskTableModel, LocalDateTime> setStartTimeTableColumn() {
        TableColumn<TaskTableModel, LocalDateTime> startTimeColumn = new TableColumn<>("Start time");
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        startTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(dateTimeFormatter.format(item));
                }
            }
        });
        return startTimeColumn;
    }

    TableColumn<TaskTableModel, String> setDescriptionTableColumn() {
        TableColumn<TaskTableModel, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        return descriptionColumn;
    }

    TableColumn<TaskTableModel, List<Employee>> setEmployeeListTableColumn() {
        TableColumn<TaskTableModel, List<Employee>> employeeColumn = new TableColumn<>("Team assigned");
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("employees"));
        employeeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(List<Employee> employees, boolean empty) {
                super.updateItem(employees, empty);
                if (employees == null || empty) {
                    setText(null);
                } else {
                    setText(
                            employees.stream()
                                    .map(employee ->
                                            employee.getName() + " " + employee.getSurname() + ", " + employee.getEmail()
                                    )
                                    .sorted()
                                    .collect(Collectors.joining("\n"))
                    );
                }
            }
        });
        return employeeColumn;
    }

    TableColumn<TaskTableModel, LocalDate> setDeadlineTableColumn() {
        TableColumn<TaskTableModel, LocalDate> deadLineColumn = new TableColumn<>("Deadline");
        deadLineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        deadLineColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });
        return deadLineColumn;
    }

    TableColumn<TaskTableModel, String> setTaskStatusTableColumn() {
        TableColumn<TaskTableModel, String> taskStatusColumn = new TableColumn<>("Status");
        taskStatusColumn.setCellValueFactory(new PropertyValueFactory<>("taskStatus"));
        taskStatusColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(TaskStatus.valueOf(item).getEngStatusName());
                }
            }
        });
        return taskStatusColumn;
    }

    TableColumn<TaskTableModel, Boolean> setCompleteTableColumn() {
        TableColumn<TaskTableModel, Boolean> completeColumn = new TableColumn<>("Complete");
        completeColumn.setCellValueFactory(new PropertyValueFactory<>("complete"));
        completeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item) {
                    setText("yes");
                } else {
                    setText("no");
                }
            }
        });
        return completeColumn;
    }

    TableColumn<TaskTableModel, LocalDateTime> setExecutionTimeTableColumn() {
        TableColumn<TaskTableModel, LocalDateTime> executionTimeColumn = new TableColumn<>("Execution time");
        executionTimeColumn.setCellValueFactory(new PropertyValueFactory<>("executionTime"));
        executionTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(dateTimeFormatter.format(item));
                }
            }
        });
        return executionTimeColumn;
    }

    void clearDoPhaseTaskTableView() {
        doPhaseTaskTableView.getItems().clear();
        doPhaseTaskTableView.getColumns().clear();
    }

    void clearActPhaseTaskTableView() {
        actPhaseTaskTableView.getItems().clear();
        actPhaseTaskTableView.getColumns().clear();
    }

    public void setPlanPhaseReadDto(PlanPhaseReadDto planPhaseReadDto) {
        this.planPhaseReadDto = planPhaseReadDto;
    }

    public void setDoPhaseReadDto(DoPhaseReadDto doPhaseReadDto) {
        this.doPhaseReadDto = doPhaseReadDto;
    }

    public void setCheckPhaseReadDto(CheckPhaseReadDto checkPhaseReadDto) {
        this.checkPhaseReadDto = checkPhaseReadDto;
    }

    public void setActPhaseReadDto(ActPhaseReadDto actPhaseReadDto) {
        this.actPhaseReadDto = actPhaseReadDto;
    }
}
