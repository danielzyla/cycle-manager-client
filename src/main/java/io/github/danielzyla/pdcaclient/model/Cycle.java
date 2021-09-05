package io.github.danielzyla.pdcaclient.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cycle {
    private long id;
    private String cycleName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean complete;
    private boolean nextCycle;

    @Override
    public String toString() {
        return cycleName;
    }
}
