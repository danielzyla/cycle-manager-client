package io.github.danielzyla.pdcaclient.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class Project {
    private long id;
    private LocalDateTime startTime;
    private String projectName, projectCode;
    private Set<Department> departments = new HashSet<>();
    private Set<Product> products = new HashSet<>();
    private Set<Cycle> cycles = new LinkedHashSet<>();
    private LocalDateTime endTime;
    private boolean complete;
}
