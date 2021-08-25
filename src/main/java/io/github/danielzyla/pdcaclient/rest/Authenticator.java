package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.dto.UserWriteDto;
import io.github.danielzyla.pdcaclient.handler.AuthenticationResultHandler;

public interface Authenticator {
    void authenticate(UserWriteDto userWriteDto, AuthenticationResultHandler authenticationResultHandler);
}
