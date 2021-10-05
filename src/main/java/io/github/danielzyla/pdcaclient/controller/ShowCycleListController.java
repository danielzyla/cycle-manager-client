package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.ProjectReadDto;
import io.github.danielzyla.pdcaclient.model.Cycle;
import io.github.danielzyla.pdcaclient.model.ProjectTableModel;
import io.github.danielzyla.pdcaclient.rest.CycleRestClient;
import io.github.danielzyla.pdcaclient.rest.ProjectRestClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ShowCycleListController implements Initializable {

    @FXML
    private Label projectNameLabel;
    @FXML
    private ListView<Cycle> cycleListView;
    @FXML
    private Label cycleNameLabel;
    @FXML
    private VBox cycleViewVBox;
    @FXML
    private Button nextCycleButton;

    private final static String VIEW_FXML_PATH = "/io/github/danielzyla/pdcaclient/fxml";
    private final ProjectRestClient projectRestClient;
    private final CycleRestClient cycleRestClient;
    private final String token;
    private final ProjectTableModel selectedProject;
    private ProjectReadDto projectReadDto;

    public void setProjectReadDto(ProjectReadDto projectReadDto) {
        this.projectReadDto = projectReadDto;
    }

    public ShowCycleListController(ProjectTableModel selectedProject, String token) {
        this.projectRestClient = new ProjectRestClient();
        this.cycleRestClient = new CycleRestClient();
        this.selectedProject = selectedProject;
        this.token = token;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        projectNameLabel.setText(selectedProject.getProjectName());
        try {
            initializeNextCycleButton();
            initializeCycleListView();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        initializeSelectedCycleView();
    }

    void initializeNextCycleButton() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                this.setProjectReadDto(projectRestClient.getById(this.token, selectedProject.getId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
        thread.join();

        Comparator<Cycle> cyclesByName = Comparator.comparing(Cycle::getCycleName);
        List<Cycle> cycles = projectReadDto.getCycles().stream().sorted(cyclesByName).collect(Collectors.toList());
        Cycle lastCycle = cycles.get(cycles.size() - 1);
        if (lastCycle.isComplete() && lastCycle.isNextCycle()) {
            nextCycleButton.setVisible(true);
            nextCycleButton.setOnAction(createNextCycleAction -> {
                try {
                    cycleRestClient.createNextCycle(this.token, selectedProject.getId(), () -> {
                        this.initializeCycleListView();
                        nextCycleButton.setVisible(false);
                    });
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void initializeSelectedCycleView() {
        cycleListView.setOnMouseClicked(mouseEvent -> {
            final Cycle selectedItem = cycleListView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                cycleNameLabel.setText(selectedItem.getCycleName());
                try {
                    loadSelectedCycleView(selectedItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadSelectedCycleView(Cycle selected) throws IOException {
        cycleViewVBox.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(VIEW_FXML_PATH + "/cycle-view.fxml")
        );
        CycleViewController cycleViewController = new CycleViewController(selected, token, this);
        loader.setController(cycleViewController);
        BorderPane borderPane = loader.load();
        cycleViewVBox.getChildren().add(borderPane);
    }

    void initializeCycleListView() throws InterruptedException {
        ObservableList<Cycle> data = FXCollections.observableArrayList();
        Thread thread = new Thread(() -> {
            try {
                data.addAll(projectRestClient.getById(token, selectedProject.getId()).getCycles());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
        thread.join();
        cycleListView.setItems(data);
        System.out.println("data" + data);
        cycleListView.getItems().sort(
                ((cycle1, cycle2) ->
                        String.CASE_INSENSITIVE_ORDER.compare(cycle1.getCycleName(), cycle2.getCycleName()))
        );
    }
}
