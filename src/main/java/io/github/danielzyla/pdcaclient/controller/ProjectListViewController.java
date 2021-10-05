package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.model.Cycle;
import io.github.danielzyla.pdcaclient.model.Department;
import io.github.danielzyla.pdcaclient.model.Product;
import io.github.danielzyla.pdcaclient.model.ProjectTableModel;
import io.github.danielzyla.pdcaclient.rest.ProjectRestClient;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProjectListViewController implements Initializable {

    @FXML
    private BorderPane projectListBorderPane;
    @FXML
    private GridPane projectListGridPane;
    @FXML
    private ScrollPane projectListScrollPane;
    @FXML
    private TableView<ProjectTableModel> projectListTableView;
    @FXML
    private Label nameLabel;
    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button cycleButton;
    @FXML
    private Button deleteButton;

    private final ProjectRestClient projectRestClient;
    private String token;

    public ProjectListViewController() {
        this.projectRestClient = new ProjectRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.setText("Project list");
        try {
            setProjectListTableView();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        createButton.setOnAction(createProjectAction -> initializeCreateNewProjectStage());
        editButton.setOnAction(editAction -> initializeEditProjectStage());
        cycleButton.setOnAction(showCycle -> initializeShowCycleStage());
        deleteButton.setOnAction(deleteAction -> initializeDeleteProjectStage());
    }

    private void initializeShowCycleStage() {
        ProjectTableModel selectedProject = projectListTableView.getSelectionModel().getSelectedItem();
        try {
            Stage createCycleStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/io/github/danielzyla/pdcaclient/fxml/show-cycle-list.fxml"
            ));
            ShowCycleListController controller = new ShowCycleListController(selectedProject, getToken());
            loader.setController(controller);
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setStyle("-fx-background: rgba(125, 173, 216);");
            Parent parent = loader.load();
            scrollPane.setContent(parent);
            Scene scene = new Scene(scrollPane, 1024, 768);
            createCycleStage.setScene(scene);
            createCycleStage.setWidth(scene.getWidth());
            scrollPane.setFitToWidth(true);
            createCycleStage.initModality(Modality.WINDOW_MODAL);
            createCycleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeCreateNewProjectStage() {
        try {
            Stage createProjectStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/io/github/danielzyla/pdcaclient/fxml/add-project.fxml"
            ));
            Parent parent = loader.load();
            Scene scene = new Scene(parent, 600, 500);
            createProjectStage.setScene(scene);
            createProjectStage.initModality(Modality.APPLICATION_MODAL);
            createProjectStage.initStyle(StageStyle.UNDECORATED);
            CreateProjectController controller = loader.getController();
            controller.setToken(getToken());
            controller.setController(this);
            createProjectStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void setProjectListTableView() throws InterruptedException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        TableColumn<ProjectTableModel, LocalDateTime> startTimeColumn = new TableColumn<>("Start time");
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

        TableColumn<ProjectTableModel, String> projectNameColumn = new TableColumn<>("Project name");
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));

        TableColumn<ProjectTableModel, String> projectCodeColumn = new TableColumn<>("Project code");
        projectCodeColumn.setCellValueFactory(new PropertyValueFactory<>("projectCode"));

        TableColumn<ProjectTableModel, List<Department>> departmentsColumn = new TableColumn<>("Departments");
        departmentsColumn.setCellValueFactory(new PropertyValueFactory<>("departments"));
        departmentsColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(List<Department> departments, boolean empty) {
                super.updateItem(departments, empty);
                if (departments == null || empty) {
                    setText(null);
                } else {
                    setText(
                            departments.stream()
                                    .map(Department::getDeptName)
                                    .sorted()
                                    .collect(Collectors.joining("\n"))
                    );
                }
            }
        });

        TableColumn<ProjectTableModel, List<Product>> productsColumn = new TableColumn<>("Products");
        productsColumn.setCellValueFactory(new PropertyValueFactory<>("products"));
        productsColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(List<Product> products, boolean empty) {
                super.updateItem(products, empty);
                if (products == null || empty) {
                    setText(null);
                } else {
                    setText(
                            products.stream()
                                    .map(Product::getProductName)
                                    .sorted()
                                    .collect(Collectors.joining("\n"))
                    );
                }
            }
        });

        TableColumn<ProjectTableModel, List<Cycle>> cyclesColumn = new TableColumn<>("Cycles");
        cyclesColumn.setCellValueFactory(new PropertyValueFactory<>("cycles"));
        cyclesColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(List<Cycle> cycles, boolean empty) {
                super.updateItem(cycles, empty);
                if (cycles == null || empty) {
                    setText(null);
                } else {
                    setText(
                            cycles.stream()
                                    .map(cycle -> cycle.getCycleName()
                                            + " started: " + dateTimeFormatter.format(cycle.getStartTime()))
                                    .sorted()
                                    .collect(Collectors.joining("\n"))
                    );
                }
            }
        });

        TableColumn<ProjectTableModel, Boolean> completeColumn = new TableColumn<>("Complete");
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

        TableColumn<ProjectTableModel, LocalDateTime> endTimeColumn = new TableColumn<>("End time");
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        endTimeColumn.setCellFactory(column -> new TableCell<>() {
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

        projectListTableView.getColumns().addAll(
                startTimeColumn,
                projectNameColumn,
                projectCodeColumn,
                departmentsColumn,
                productsColumn,
                cyclesColumn,
                completeColumn,
                endTimeColumn
        );

        ObservableList<ProjectTableModel> data = FXCollections.observableArrayList();
        loadProjectData(data);
        projectListTableView.setItems(data);
    }


    public void loadProjectData(final ObservableList<ProjectTableModel> data) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                data.addAll(
                        projectRestClient.getProjects(getToken()).stream()
                                .map(ProjectTableModel::new)
                                .collect(Collectors.toList())
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
        thread.join();
    }

    private void initializeEditProjectStage() {
        ProjectTableModel selectedProject = projectListTableView.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            try {
                Stage editProjectStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/edit-project.fxml"
                ));
                EditProjectController controller = new EditProjectController(getToken(), selectedProject, this);
                loader.setController(controller);
                Parent parent = loader.load();
                Scene scene = new Scene(parent, 600, 500);
                editProjectStage.setScene(scene);
                editProjectStage.initModality(Modality.APPLICATION_MODAL);
                editProjectStage.initStyle(StageStyle.UNDECORATED);
                editProjectStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeDeleteProjectStage() {
        ProjectTableModel selectedProject = projectListTableView.getSelectionModel().getSelectedItem();
        if (selectedProject != null) {
            try {
                Stage deleteProjectStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                        "/io/github/danielzyla/pdcaclient/fxml/delete-project.fxml"
                ));
                Parent parent = loader.load();
                Scene scene = new Scene(parent, 400, 250);
                deleteProjectStage.setScene(scene);
                deleteProjectStage.initModality(Modality.APPLICATION_MODAL);
                deleteProjectStage.initStyle(StageStyle.UNDECORATED);
                DeleteProjectController controller = loader.getController();
                controller.setToken(getToken());
                controller.loadProjectData(selectedProject);
                controller.setController(this);
                deleteProjectStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshProjectList() throws InterruptedException {
        projectListTableView.getItems().clear();
        projectListTableView.getColumns().clear();
        setProjectListTableView();
    }

    String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
