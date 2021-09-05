package io.github.danielzyla.pdcaclient.dto;

import lombok.Data;

@Data
public class DoPhaseWriteApiDto {
    private long id;
    private String description;
    private boolean complete;
}
