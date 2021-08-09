package io.github.danielzyla.pdcaclient.rest;

import io.github.danielzyla.pdcaclient.dto.UserWriteDto;

public interface Authenticator {
    void authenticate(UserWriteDto userWriteDto, AuthenticationResultHandler authenticationResultHandler);
}
