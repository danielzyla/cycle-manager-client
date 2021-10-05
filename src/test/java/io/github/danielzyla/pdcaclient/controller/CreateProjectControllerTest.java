package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.DepartmentReadDto;
import io.github.danielzyla.pdcaclient.dto.ProductReadDto;
import io.github.danielzyla.pdcaclient.dto.ProjectWriteApiDto;
import io.github.danielzyla.pdcaclient.handler.CrudOperationResultHandler;
import io.github.danielzyla.pdcaclient.rest.DepartmentRestClient;
import io.github.danielzyla.pdcaclient.rest.ProductRestClient;
import io.github.danielzyla.pdcaclient.rest.ProjectRestClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.matcher.control.ListViewMatchers;
import org.testfx.util.WaitForAsyncUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
class CreateProjectControllerTest {

    @Mock
    private ProjectRestClient projectRestClientMock;

    @Mock
    private DepartmentRestClient departmentRestClientMock;

    @Mock
    private ProductRestClient productRestClientMock;

    @InjectMocks
    private CreateProjectController controller;

    @Start
    void start(Stage createProjectStage) {
        try {
            createProjectStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/io/github/danielzyla/pdcaclient/fxml/add-project.fxml"
            ));
            Parent parent = loader.load();
            Scene scene = new Scene(parent, 600, 500);
            createProjectStage.setScene(scene);
            createProjectStage.initModality(Modality.APPLICATION_MODAL);
            createProjectStage.initStyle(StageStyle.UNDECORATED);
            controller = loader.getController();
            controller.setToken("test-token");
            createProjectStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    void initializeDepartmentsButton_ShouldEnableDepartmentListView(FxRobot robot) throws IllegalAccessException, IOException, NoSuchFieldException {
        //given
        getDepartmentData();

        //when
        robot.clickOn("#departmentsButton");
        WaitForAsyncUtils.waitForFxEvents();

        //then
        FxAssert.verifyThat("#departmentsListView", Predicate.not(Node::isDisabled));
        robot.clickOn("#cancelButton");
    }

    @Test
    @Order(2)
    void loadDepartmentList_ShouldLoadObservableDepartmentListIntoDepartmentListView(FxRobot robot) throws IOException, NoSuchFieldException, IllegalAccessException {
        //given
        getDepartmentData();

        //when
        robot.clickOn("#departmentsButton");
        WaitForAsyncUtils.waitForFxEvents();

        //then
        FxAssert.verifyThat("#departmentsListView", ListViewMatchers.hasItems(2));
        robot.clickOn("#cancelButton");
    }

    private void getDepartmentData() throws NoSuchFieldException, IllegalAccessException, IOException {
        setDepartmentRestClientAccess(departmentRestClientMock);
        List<DepartmentReadDto> departmentReadDtoList = new ArrayList<>();
        DepartmentReadDto readDto1 = new DepartmentReadDto();
        readDto1.setId(1);
        readDto1.setDeptName("department-1");
        DepartmentReadDto readDto2 = new DepartmentReadDto();
        readDto2.setId(2);
        readDto2.setDeptName("department-2");
        departmentReadDtoList.add(readDto1);
        departmentReadDtoList.add(readDto2);
        given(departmentRestClientMock.getDepartments("test-token")).willReturn(departmentReadDtoList);
    }

    private void setDepartmentRestClientAccess(DepartmentRestClient departmentRestClientMock) throws NoSuchFieldException, IllegalAccessException {
        Field departmentRestClient = CreateProjectController.class.getDeclaredField("departmentRestClient");
        departmentRestClient.setAccessible(true);
        departmentRestClient.set(controller, departmentRestClientMock);
    }

    @Test
    @Order(3)
    void initializeProductsButton_ShouldEnableProductListView(FxRobot robot) throws IllegalAccessException, IOException, NoSuchFieldException {
        //given
        getProductData();

        //when
        robot.clickOn("#productsButton");
        WaitForAsyncUtils.waitForFxEvents();

        //then
        FxAssert.verifyThat("#productsListView", Predicate.not(Node::isDisabled));
        robot.clickOn("#cancelButton");
    }

    @Test
    @Order(4)
    void loadProductList_ShouldLoadObservableProductListIntoProductListView(FxRobot robot) throws IOException, NoSuchFieldException, IllegalAccessException {
        //given
        getProductData();

        //when
        robot.clickOn("#productsButton");
        WaitForAsyncUtils.waitForFxEvents();

        //then
        FxAssert.verifyThat("#productsListView", ListViewMatchers.hasItems(2));
        robot.clickOn("#cancelButton");
    }

    private void getProductData() throws NoSuchFieldException, IllegalAccessException, IOException {
        setProductRestClientAccess(productRestClientMock);
        List<ProductReadDto> productReadDtoList = new ArrayList<>();
        ProductReadDto readDto1 = new ProductReadDto();
        readDto1.setId(1);
        readDto1.setProductName("product-1");
        ProductReadDto readDto2 = new ProductReadDto();
        readDto2.setId(2);
        readDto2.setProductName("product-2");
        productReadDtoList.add(readDto1);
        productReadDtoList.add(readDto2);
        given(productRestClientMock.getProducts("test-token")).willReturn(productReadDtoList);
    }

    private void setProductRestClientAccess(ProductRestClient productRestClientMock) throws NoSuchFieldException, IllegalAccessException {
        Field productRestClient = CreateProjectController.class.getDeclaredField("productRestClient");
        productRestClient.setAccessible(true);
        productRestClient.set(controller, productRestClientMock);
    }

    @Test
    @Order(5)
    void initializeSaveButton_WhenAllProjectFieldsAreGiven_ShouldCallSaveProjectMethodOnProjectRestClient(FxRobot robot) throws NoSuchFieldException, IllegalAccessException, IOException, InterruptedException {
        //given
        setProjectRestClientAccess(projectRestClientMock);
        getDepartmentData();
        getProductData();
        robot.clickOn("#projectNameTextField");
        robot.write("project-1");
        robot.clickOn("#projectCodeTextField");
        robot.write("1-2021");
        robot.clickOn("#departmentsButton");
        robot.clickOn("#departmentsListView")
                .press(KeyCode.CONTROL)
                .clickOn("department-1")
                .clickOn("department-2")
                .release(KeyCode.CONTROL);
        robot.clickOn("#productsButton");
        robot.clickOn("#productsListView")
                .press(KeyCode.CONTROL)
                .clickOn("product-1")
                .clickOn("product-2")
                .release(KeyCode.CONTROL);

        //when
        robot.clickOn("#saveButton");
        WaitForAsyncUtils.waitForFxEvents();

        //then
        verify(projectRestClientMock).saveProject(anyString(), any(ProjectWriteApiDto.class), any(CrudOperationResultHandler.class));
        robot.clickOn("#cancelButton");
    }

    @Test
    @Order(6)
    void initializeSaveButton_WhenBasicProjectFieldsAreGiven_ShouldCallSaveProjectMethodOnProjectRestClient(FxRobot robot) throws NoSuchFieldException, IllegalAccessException, IOException, InterruptedException {
        //given
        setProjectRestClientAccess(projectRestClientMock);
        robot.clickOn("#projectNameTextField");
        robot.write("project-1");
        robot.clickOn("#projectCodeTextField");
        robot.write("1-2021");

        //when
        robot.clickOn("#saveButton");
        WaitForAsyncUtils.waitForFxEvents();

        //then
        verify(projectRestClientMock).saveProject(anyString(), any(ProjectWriteApiDto.class), any(CrudOperationResultHandler.class));
        robot.clickOn("#cancelButton");
    }

    private void setProjectRestClientAccess(ProjectRestClient projectRestClientMock) throws NoSuchFieldException, IllegalAccessException {
        Field projectRestClient = CreateProjectController.class.getDeclaredField("projectRestClient");
        projectRestClient.setAccessible(true);
        projectRestClient.set(controller, projectRestClientMock);
    }

    @Test
    @Order(7)
    void validateProjectName_WhenProjectSavedWithoutNameValue_ShouldCallValidationAlert(FxRobot robot) {
        //given
        robot.clickOn("#projectCodeTextField");
        robot.write("1-2021");

        //when
        robot.clickOn("#saveButton");
        WaitForAsyncUtils.waitForFxEvents();

        //then
        DialogPane dialogPane = robot.lookup(".dialog-pane").query();
        FxAssert.verifyThat(dialogPane.getContentText(), startsWith("Please enter valid "));
        FxAssert.verifyThat(dialogPane.getContentText(), equalTo("Please enter valid project name"));

        robot.clickOn(".button");
        robot.clickOn("#cancelButton");
    }

    @Test
    @Order(8)
    void validateProjectCode_WhenProjectSavedWithoutCodeValue_ShouldCallValidationAlert(FxRobot robot) {
        //given
        robot.clickOn("#projectNameTextField");
        robot.write("project-1");

        //when
        robot.clickOn("#saveButton");
        WaitForAsyncUtils.waitForFxEvents();

        //then
        DialogPane dialogPane = robot.lookup(".dialog-pane").query();
        FxAssert.verifyThat(dialogPane.getContentText(), startsWith("Please enter valid "));
        FxAssert.verifyThat(dialogPane.getContentText(), equalTo("Please enter valid project code"));

        robot.clickOn(".button");
        robot.clickOn("#cancelButton");
    }
}