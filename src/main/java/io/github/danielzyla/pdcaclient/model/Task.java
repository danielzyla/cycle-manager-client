package io.github.danielzyla.pdcaclient.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class Task {
    private long id;
    private LocalDateTime startTime;
    private String description;
    private Set<Employee> employees = new HashSet<>();
    private LocalDate deadline;
    private String taskStatus;
    private boolean complete;
    private LocalDateTime executionTime;
}
