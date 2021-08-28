package io.github.danielzyla.pdcaclient.model;

import java.util.HashSet;
import java.util.Set;

public class Employee {
    private long id;
    private String name, surname;
    private String email;
    private Set<PlanPhase> planPhases = new HashSet<>();
    private Set<Task> tasks = new HashSet<>();
    private Department department;
}
