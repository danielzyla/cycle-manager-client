package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.model.Cycle;
import io.github.danielzyla.pdcaclient.model.Department;
import io.github.danielzyla.pdcaclient.model.Product;
import io.github.danielzyla.pdcaclient.model.ProjectTableModel;
import io.github.danielzyla.pdcaclient.rest.ProjectRestClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    private final ProjectRestClient projectRestClient;
    private final String token;
    @FXML
    private MenuItem openProjectsMenuItem;
    @FXML
    private VBox projectListVBox;
    @FXML
    private TableView<ProjectTableModel> projectTableView;
    @FXML
    private Button projectListExitButton;


    public MainController(String token) {
        this.projectRestClient = new ProjectRestClient();
        this.token = token;
    }

    String getToken() {
        return token;
    }

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        openProjectsMenuItem.setOnAction(
                actionEvent -> {
                    projectListVBox.setVisible(true);
                    initializeOpenProjectsItem();
                    openProjectsMenuItem.setDisable(true);
                }
        );
        projectListExitButton.setOnAction(
                event -> {
                    projectListVBox.setVisible(false);
                    openProjectsMenuItem.setDisable(false);
                    projectTableView.getItems().clear();
                    projectTableView.getColumns().clear();
                }
        );
    }

    public void initializeOpenProjectsItem() {
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

        projectTableView.getColumns().addAll(
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
        projectTableView.setItems(data);
    }

    public void loadProjectData(final ObservableList<ProjectTableModel> data) {
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
    }
}