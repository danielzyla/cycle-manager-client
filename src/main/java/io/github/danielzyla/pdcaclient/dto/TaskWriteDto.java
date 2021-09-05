package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.TaskStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskWriteDto {
    private long id;
    private LocalDateTime startTime;
    private String description;
    private List<Long> employeeIds;
    private LocalDate deadline;
    private TaskStatus taskStatus;
    private boolean complete;
    private LocalDateTime executionTime;
}
