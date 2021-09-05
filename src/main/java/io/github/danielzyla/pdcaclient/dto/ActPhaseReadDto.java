package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.ActPhaseTask;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActPhaseReadDto {
    private long id;
    private LocalDateTime startTime;
    private String description;
    private List<ActPhaseTask> actPhaseTasks;
    private boolean complete;
    private boolean nextCycle;
    private LocalDateTime endTime;
}
