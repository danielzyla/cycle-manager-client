package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.Department;
import io.github.danielzyla.pdcaclient.model.PlanPhase;
import lombok.Data;

import java.util.List;

@Data
public class EmployeeReadDto {
    private long id;
    private String name, surname, email;
    private List<PlanPhase> planPhases;
    private Department department;

    @Override
    public String toString() {
        return name + " " + surname + ", email='" + email + "'" + ", department=" + department.getDeptName();
    }
}
