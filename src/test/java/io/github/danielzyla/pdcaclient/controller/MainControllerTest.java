package io.github.danielzyla.pdcaclient.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.lang.reflect.Field;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
class MainControllerTest {

    @Mock
    private ProjectListViewController projectListViewControllerMock;

    @Mock
    private ProductListViewController productListViewControllerMock;

    @Mock
    private DepartmentListViewController departmentListViewControllerMock;

    @Mock
    private EmployeeListViewController employeeListViewControllerMock;

    @InjectMocks
    private MainController controller;

    @Start
    void start(Stage appStage) {
        appStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/io/github/danielzyla/pdcaclient/fxml/main-app-view.fxml"
            ));
            Parent appRoot = loader.load();
            Scene scene = new Scene(appRoot, 1024, 768);
            appStage.setTitle("pdca-App");
            appStage.setScene(scene);
            controller = loader.getController();
            controller.setToken("test-token");
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void initializeChosenMenuItem_WhenOpenProjectListMenuItemClicked_ShouldCallLoadProjectListViewMethod(FxRobot robot) throws NoSuchFieldException, IllegalAccessException {
        //given
        Field projectListViewController = MainController.class.getDeclaredField("projectListViewController");
        projectListViewController.setAccessible(true);
        projectListViewController.set(controller, projectListViewControllerMock);

        //when
        robot.clickOn("File");
        robot.clickOn("Open...");
        robot.clickOn("#openProjectListMenuItem");

        //then
        /*The ProjectListView screen should be displayed after calling 'loadProjectListView' method .*/
    }

    @Test
    void initializeChosenMenuItem_WhenOpenProductListMenuItemClicked_ShouldCallLoadProductListViewMethod(FxRobot robot) throws NoSuchFieldException, IllegalAccessException {
        //given
        Field productListViewController = MainController.class.getDeclaredField("productListViewController");
        productListViewController.setAccessible(true);
        productListViewController.set(controller, productListViewControllerMock);

        //when
        robot.clickOn("File");
        robot.clickOn("Open...");
        robot.moveTo("#openProjectListMenuItem");
        robot.clickOn("#openProductListMenuItem");

        //then
        /*The ProductListView screen should be displayed after calling 'loadProductListView' method .*/
    }

    @Test
    void initializeChosenMenuItem_WhenOpenDepartmentListMenuItemClicked_ShouldCallLoadDepartmentListViewMethod(FxRobot robot) throws NoSuchFieldException, IllegalAccessException {
        //given
        Field departmentListViewController = MainController.class.getDeclaredField("departmentListViewController");
        departmentListViewController.setAccessible(true);
        departmentListViewController.set(controller, departmentListViewControllerMock);

        //when
        robot.clickOn("File");
        robot.clickOn("Open...");
        robot.moveTo("#openProjectListMenuItem");
        robot.clickOn("#openDepartmentListMenuItem");

        //then
        /*The DepartmentListView screen should be displayed after calling 'loadDepartmentListView' method .*/
    }


    @Test
    void initializeChosenMenuItem_WhenOpenEmployeeListMenuItemClicked_ShouldCallLoadEmployeeListViewMethod(FxRobot robot) throws NoSuchFieldException, IllegalAccessException {
        //given
        Field employeeListViewController = MainController.class.getDeclaredField("employeeListViewController");
        employeeListViewController.setAccessible(true);
        employeeListViewController.set(controller, employeeListViewControllerMock);

        //when
        robot.clickOn("File");
        robot.clickOn("Open...");
        robot.moveTo("#openProjectListMenuItem");
        robot.clickOn("#openEmployeeListMenuItem");

        //then
        /*The EmployeeListView screen should be displayed after calling 'loadEmployeeListView' method .*/
    }

}