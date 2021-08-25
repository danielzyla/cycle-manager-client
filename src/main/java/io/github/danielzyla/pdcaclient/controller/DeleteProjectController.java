package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.model.ProjectTableModel;
import io.github.danielzyla.pdcaclient.rest.ProjectRestClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeleteProjectController implements Initializable {

    @FXML
    private BorderPane deleteProjectBorderPane;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Label projectNameLabel;
    @FXML
    private Label projectCodeLabel;
    private Long projectId;
    private final ProjectRestClient projectRestClient;
    private String token;
    private MainController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeDeleteButton();
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(deleteAction -> {
            Thread thread = new Thread(() -> {
                try {
                    projectRestClient.deleteProject(getToken(), projectId, () -> Platform.runLater(() -> {
                        getStage().close();
                        try {
                            this.mainController.refreshProjectList();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
        });
    }

    public void loadProjectData(ProjectTableModel projectTableModel) {
        this.projectId = projectTableModel.getId();
        projectNameLabel.setText("name: " + projectTableModel.getProjectName());
        projectCodeLabel.setText("code: " + projectTableModel.getProjectCode());
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(cancelAction -> getStage().close());
    }

    private Stage getStage() {
        return (Stage) deleteProjectBorderPane.getScene().getWindow();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public DeleteProjectController() {
        this.projectRestClient = new ProjectRestClient();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
