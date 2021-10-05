package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.dto.UserWriteDto;
import io.github.danielzyla.pdcaclient.handler.AuthenticationResultHandler;
import javafx.stage.Stage;

public interface Authenticator {
    void authenticate(UserWriteDto user, AuthenticationResultHandler authenticationResultHandler, Stage waitingPopupStage);
}
