package io.github.danielzyla.pdcaclient.dto;

import lombok.Data;

@Data
public class ActPhaseWriteApiDto {
    private long id;
    private String description;
    private boolean complete;
    private boolean nextCycle;
}
