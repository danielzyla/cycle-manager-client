package io.github.danielzyla.pdcaclient.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private final static String VIEW_FXML_PATH = "/io/github/danielzyla/pdcaclient/fxml";
    private String token;
    private final ProjectListViewController projectListViewController;
    private final ProductListViewController productListViewController;
    private final DepartmentListViewController departmentListViewController;
    private final EmployeeListViewController employeeListViewController;

    @FXML
    private BorderPane mainAppBorderPane;
    @FXML
    private MenuItem openProjectListMenuItem;
    @FXML
    private MenuItem openProductListMenuItem;
    @FXML
    private MenuItem openDepartmentListMenuItem;
    @FXML
    private MenuItem openEmployeeListMenuItem;
    @FXML
    private Pane viewPane;
    @FXML
    private MenuItem quitAppMenuItem;

    public MainController() {
        this.projectListViewController = new ProjectListViewController();
        this.productListViewController = new ProductListViewController();
        this.departmentListViewController = new DepartmentListViewController();
        this.employeeListViewController = new EmployeeListViewController();
    }

    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        initializeChosenMenuItem();
        quitAppMenuItem.setOnAction(closeApp -> getStage().close());
    }

    private void initializeChosenMenuItem() {
        openProjectListMenuItem.setOnAction(openProjectList -> loadProjectListView());
        openProductListMenuItem.setOnAction((openProductList -> loadProductListView()));
        openDepartmentListMenuItem.setOnAction(openDepartmentList -> loadDepartmentListView());
        openEmployeeListMenuItem.setOnAction(openEmployeeList -> loadEmployeeListView());
    }

    private void loadProjectListView() {
        viewPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_FXML_PATH + "/project-list-view.fxml"));
        loader.setController(projectListViewController);
        projectListViewController.setToken(getToken());
        BorderPane borderPane = null;
        try {
            borderPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewPane.getChildren().add(borderPane);
        assert borderPane != null;
        borderPane.prefWidthProperty().bind(viewPane.widthProperty());
        borderPane.prefHeightProperty().bind(viewPane.heightProperty());
    }

    private void loadProductListView() {
        viewPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_FXML_PATH + "/product-list-view.fxml"));
        loader.setController(productListViewController);
        productListViewController.setToken(getToken());
        BorderPane borderPane = null;
        try {
            borderPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewPane.getChildren().add(borderPane);
        assert borderPane != null;
        borderPane.prefWidthProperty().bind(viewPane.widthProperty());
        borderPane.prefHeightProperty().bind(viewPane.heightProperty());
    }

    private void loadDepartmentListView() {
        viewPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_FXML_PATH + "/department-list-view.fxml"));
        loader.setController(departmentListViewController);
        departmentListViewController.setToken(getToken());
        BorderPane borderPane = null;
        try {
            borderPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewPane.getChildren().add(borderPane);
        assert borderPane != null;
        borderPane.prefWidthProperty().bind(viewPane.widthProperty());
        borderPane.prefHeightProperty().bind(viewPane.heightProperty());
    }

    private void loadEmployeeListView() {
        viewPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_FXML_PATH + "/employee-list-view.fxml"));
        loader.setController(employeeListViewController);
        employeeListViewController.setToken(getToken());
        BorderPane borderPane = null;
        try {
            borderPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        viewPane.getChildren().add(borderPane);
        assert borderPane != null;
        borderPane.prefWidthProperty().bind(viewPane.widthProperty());
        borderPane.prefHeightProperty().bind(viewPane.heightProperty());
    }

    String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private Stage getStage() {
        return (Stage) mainAppBorderPane.getScene().getWindow();
    }

}