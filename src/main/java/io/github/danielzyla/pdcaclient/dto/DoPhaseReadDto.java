package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.DoPhaseTask;

import java.time.LocalDateTime;
import java.util.List;

public class DoPhaseReadDto {
    private long id;
    private LocalDateTime startTime;
    private String description;
    private List<DoPhaseTask> doPhaseTasks;
    private boolean complete;
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

    public List<DoPhaseTask> getDoPhaseTasks() {
        return doPhaseTasks;
    }

    public void setDoPhaseTasks(List<DoPhaseTask> doPhaseTasks) {
        this.doPhaseTasks = doPhaseTasks;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
