package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.ActPhaseTask;

import java.time.LocalDateTime;
import java.util.List;

public class ActPhaseReadDto {
    private long id;
    private LocalDateTime startTime;
    private String description;
    private List<ActPhaseTask> actPhaseTasks;
    private boolean complete;
    private boolean nextCycle;
    private LocalDateTime endTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ActPhaseTask> getActPhaseTasks() {
        return actPhaseTasks;
    }

    public void setActPhaseTasks(List<ActPhaseTask> actPhaseTasks) {
        this.actPhaseTasks = actPhaseTasks;
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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
