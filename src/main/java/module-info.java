module io.github.danielzyla.pdcaclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.web;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;

    opens io.github.danielzyla.pdcaclient to javafx.fxml;
    exports io.github.danielzyla.pdcaclient;
    exports io.github.danielzyla.pdcaclient.controller;
    opens io.github.danielzyla.pdcaclient.controller to javafx.fxml, javafx.graphics, javafx.controls, javafx.base;
    exports io.github.danielzyla.pdcaclient.model;
    opens io.github.danielzyla.pdcaclient.model to javafx.base;
    opens io.github.danielzyla.pdcaclient.dto to javafx.base;
    exports io.github.danielzyla.pdcaclient.dto;
}