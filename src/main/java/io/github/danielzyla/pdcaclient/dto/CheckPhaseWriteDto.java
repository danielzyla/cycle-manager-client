package io.github.danielzyla.pdcaclient.dto;

import lombok.Data;

@Data
public class CheckPhaseWriteDto {
    private Long id;
    private String conclusions, achievements, nextSteps;
    private boolean complete;
}
