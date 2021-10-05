package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.UserWriteDto;
import io.github.danielzyla.pdcaclient.handler.AuthenticationResultHandler;
import io.github.danielzyla.pdcaclient.rest.CustomAuthenticatorProvider;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith({ApplicationExtension.class, MockitoExtension.class})
class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;


    @Start
    private void start(Stage stage) throws IOException {
        stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/io/github/danielzyla/pdcaclient/fxml/login.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent, 600, 400);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        loginController = fxmlLoader.getController();
        stage.show();
    }

    @Test
    void performAuthentication_ShouldCallAuthenticateMethodWithGivenCredentials(FxRobot robot) throws NoSuchFieldException, IllegalAccessException {
        //given
        CustomAuthenticatorProvider authenticatorProviderMock = mock(CustomAuthenticatorProvider.class);
        Field authenticatorProvider = LoginController.class.getDeclaredField("authenticatorProvider");
        authenticatorProvider.setAccessible(true);
        authenticatorProvider.set(loginController, authenticatorProviderMock);

        robot.clickOn("#usernameTextField").write("username");
        robot.clickOn("#passwordTextField").write("password");

        ArgumentCaptor<UserWriteDto> argumentCaptor = ArgumentCaptor.forClass(UserWriteDto.class);

        //when
        robot.clickOn("#loginButton");

        //then
        verify(authenticatorProviderMock).authenticate(argumentCaptor.capture(), any(AuthenticationResultHandler.class), any(Stage.class));
        Assertions.assertEquals(argumentCaptor.getValue().getPassword(), "password");
        Assertions.assertEquals(argumentCaptor.getValue().getUsername(), "username");
    }

    @Test
    void performAuthentication_WhenTokenIsProvided_ShouldCallOpenMainAppWindow(FxRobot robot) throws NoSuchFieldException, IllegalAccessException {
        //given
        CustomAuthenticatorProvider customAuthenticatorProvider = new CustomAuthenticatorProvider();
        Field authenticatorProvider = LoginController.class.getDeclaredField("authenticatorProvider");
        authenticatorProvider.setAccessible(true);
        authenticatorProvider.set(loginController, customAuthenticatorProvider);

        RestTemplate restTemplateMock = mock(RestTemplate.class);
        Field restTemplate = CustomAuthenticatorProvider.class.getDeclaredField("restTemplate");
        restTemplate.setAccessible(true);
        restTemplate.set(customAuthenticatorProvider, restTemplateMock);

        given(restTemplateMock.postForEntity(anyString(), any(UserWriteDto.class), any(Class.class)))
                .willReturn(new ResponseEntity<>("test-token ", HttpStatus.OK));

        //when
        robot.clickOn("#loginButton");

        //then
        /*The main app screen should be displayed after calling 'openMainAppWindow' method .*/
    }

}