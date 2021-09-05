package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.Employee;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlanPhaseReadDto {
    private long id;
    private LocalDateTime startTime;
    private String name;
    private String problemDescription;
    private String currentSituationAnalysis;
    private String goal;
    private String rootCauseIdentification;
    private String optimalSolutionChoice;
    private List<Employee> employees;
    private LocalDateTime endTime;
    private boolean complete;
}
