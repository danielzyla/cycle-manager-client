package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.Department;
import io.github.danielzyla.pdcaclient.model.PlanPhase;
import io.github.danielzyla.pdcaclient.model.Task;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeReadDto {
    private long id;
    private String name, surname, email;
    private List<PlanPhase> planPhases;
    private List<Task> tasks;
    private Department department;
}
