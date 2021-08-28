package io.github.danielzyla.pdcaclient.controller;

import io.github.danielzyla.pdcaclient.dto.UserWriteDto;
import io.github.danielzyla.pdcaclient.rest.CustomAuthenticatorProvider;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private final CustomAuthenticatorProvider authenticatorProvider;
    @FXML
    private AnchorPane loginAnchorPane;
    @FXML
    private Button exitButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;

    public LoginController() {
        this.authenticatorProvider = new CustomAuthenticatorProvider();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        closeLogInPanel();
        actionOnLoginButton();
    }

    private void actionOnLoginButton() {
        loginButton.setOnAction(event -> {
            try {
                performAuthentication();
            } catch (FailedLoginException e) {
                e.printStackTrace();
            }
        });
    }

    private void performAuthentication() throws FailedLoginException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        UserWriteDto user = new UserWriteDto();
        user.setUsername(username);
        user.setPassword(password);
        authenticatorProvider.authenticate(
                user,
                (token) -> Platform.runLater(
                        () -> {
                            try {
                                openMainAppWindow(token);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            getStage().close();
                        }
                )
        );
    }

    private void openMainAppWindow(String token) throws IOException {
        Stage appStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/io/github/danielzyla/pdcaclient/fxml/main-app-view.fxml"
            ));
            Parent appRoot = loader.load();
            Scene scene = new Scene(appRoot, 1024, 768);
            appStage.setTitle("pdca-App");
            appStage.setScene(scene);
            MainController controller = loader.getController();
            controller.setToken(token);
            appStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeLogInPanel() {
        exitButton.setOnAction(event -> getStage().close());
    }

    private Stage getStage() {
        return (Stage) loginAnchorPane.getScene().getWindow();
    }
}
