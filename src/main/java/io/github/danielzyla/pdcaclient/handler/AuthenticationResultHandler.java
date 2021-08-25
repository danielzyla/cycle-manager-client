package io.github.danielzyla.pdcaclient.handler;

import java.io.IOException;

@FunctionalInterface
public interface AuthenticationResultHandler {
    void handle(String token) throws IOException;
}
