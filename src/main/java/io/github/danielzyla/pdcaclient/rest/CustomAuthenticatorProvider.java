package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.config.PropertyProvider;
import io.github.danielzyla.pdcaclient.dto.UserWriteDto;
import io.github.danielzyla.pdcaclient.handler.AuthenticationResultHandler;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class CustomAuthenticatorProvider implements Authenticator {

    private static final String LOGIN_PATH = "/login";
    private final RestTemplate restTemplate;

    public CustomAuthenticatorProvider() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public void authenticate(final UserWriteDto user, AuthenticationResultHandler authenticationResultHandler, Stage waitingPopupStage) {
        Thread authenticationThread = new Thread(() -> {
            try {
                ResponseEntity<String> authenticationResponse = restTemplate.postForEntity(
                        PropertyProvider.getRestAppUrl() + LOGIN_PATH,
                        user,
                        String.class
                );
                authenticationResultHandler.handle(authenticationResponse.getBody());
            } catch (HttpClientErrorException.Forbidden | IOException e) {
                Platform.runLater(() -> showErrorAlert(
                        "Bad credentials !",
                        "Invalid username or password !",
                        waitingPopupStage
                ));
            } catch (ResourceAccessException e) {
                Platform.runLater(() -> showErrorAlert(
                        "No connection !",
                        "Connection failed, check and try again",
                        waitingPopupStage
                ));
            }
        });
        authenticationThread.setDaemon(true);
        authenticationThread.start();
    }

    private void showErrorAlert(String title, String text, Stage waitingPopupStage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
        waitingPopupStage.close();
    }

}
