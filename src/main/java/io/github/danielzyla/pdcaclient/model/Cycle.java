package io.github.danielzyla.pdcaclient.model;

import java.time.LocalDateTime;

public class Cycle {
    private long id;
    private String cycleName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean complete;
    private boolean nextCycle;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCycleName() {
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isNextCycle() {
        return nextCycle;
    }

    public void setNextCycle(boolean nextCycle) {
        this.nextCycle = nextCycle;
    }

    @Override
    public String toString() {
        return cycleName;
    }
}
