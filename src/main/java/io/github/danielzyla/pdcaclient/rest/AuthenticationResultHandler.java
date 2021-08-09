package io.github.danielzyla.pdcaclient.rest;

import java.io.IOException;

@FunctionalInterface
public interface AuthenticationResultHandler {
    void handle(String token) throws IOException;
}
