package io.github.danielzyla.pdcaclient.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class PlanPhase {
    private long id;
    private LocalDateTime startTime;
    private String name,
            problemDescription,
            currentSituationAnalysis,
            goal,
            rootCauseIdentification,
            optimalSolutionChoice;
    private Set<Employee> employees = new HashSet<>();
    private LocalDateTime endTime;
    private boolean complete;
}