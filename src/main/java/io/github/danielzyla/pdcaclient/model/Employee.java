package io.github.danielzyla.pdcaclient.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Employee {
    private long id;
    private String name, surname;
    private String email;
    private Set<PlanPhase> planPhases = new HashSet<>();
    private Set<Task> tasks = new HashSet<>();
    private Department department;

    @Override
    public String toString() {
        return name + " " + surname + ", email='" + email + "'" + ", department=" + department.getDeptName();
    }
}
