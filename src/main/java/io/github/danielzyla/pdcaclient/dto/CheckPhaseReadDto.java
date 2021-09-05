package io.github.danielzyla.pdcaclient.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CheckPhaseReadDto {
    private long id;
    private LocalDateTime startTime;
    private String conclusions;
    private String achievements;
    private String nextSteps;
    private boolean complete;
    private LocalDateTime endTime;
}
