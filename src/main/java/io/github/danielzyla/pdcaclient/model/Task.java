package io.github.danielzyla.pdcaclient.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Task {
    private long id;
    private LocalDateTime startTime;
    private String description;
    private Set<Employee> employees = new HashSet<>();
    private Date deadline;
    private TaskStatus taskStatus;
    private boolean complete;
    private LocalDateTime executionTime;
}
