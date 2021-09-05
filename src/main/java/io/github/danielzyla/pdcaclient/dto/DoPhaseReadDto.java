package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.DoPhaseTask;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DoPhaseReadDto {
    private long id;
    private LocalDateTime startTime;
    private String description;
    private List<DoPhaseTask> doPhaseTasks;
    private boolean complete;
    private LocalDateTime endTime;
}
