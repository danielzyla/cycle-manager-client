package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.Department;
import io.github.danielzyla.pdcaclient.model.PlanPhase;

import java.util.List;

public class EmployeeReadDto {
    private long id;
    private String name, surname, email;
    private List<PlanPhase> planPhases;
    private Department department;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PlanPhase> getPlanPhases() {
        return planPhases;
    }

    public void setPlanPhases(List<PlanPhase> planPhases) {
        this.planPhases = planPhases;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return name + " " + surname + ", email='" + email + "'" + ", department=" + department.getDeptName();
    }
}
