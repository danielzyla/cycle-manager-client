package io.github.danielzyla.pdcaclient.model;

public enum TaskStatus {
    OCZEKUJE("AWAITING"), W_REALIZACJI("PENDING"), WYKONANE("DONE");

    private final String engStatusName;

    TaskStatus(String engStatusName) {
        this.engStatusName = engStatusName;
    }

    public String getEngStatusName() {
        return engStatusName;
    }
}
