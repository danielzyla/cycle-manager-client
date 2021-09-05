package io.github.danielzyla.pdcaclient.handler;

@FunctionalInterface
public interface CrudOperationResultHandler {
    void handle() throws InterruptedException;
}
