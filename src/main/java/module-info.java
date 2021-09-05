module io.github.danielzyla.pdcaclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.web;
    requires lombok;
    requires com.fasterxml.jackson.databind;


    opens io.github.danielzyla.pdcaclient to javafx.fxml;
    exports io.github.danielzyla.pdcaclient;
    exports io.github.danielzyla.pdcaclient.controller;
    exports io.github.danielzyla.pdcaclient.model;
    opens io.github.danielzyla.pdcaclient.controller to javafx.fxml;
    opens io.github.danielzyla.pdcaclient.model to javafx.base;
    exports io.github.danielzyla.pdcaclient.dto;
    opens io.github.danielzyla.pdcaclient.dto to javafx.base;
}