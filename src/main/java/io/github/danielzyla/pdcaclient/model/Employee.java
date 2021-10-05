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

    public Set<PlanPhase> getPlanPhases() {
        return planPhases;
    }

    public void setPlanPhases(Set<PlanPhase> planPhases) {
        this.planPhases = planPhases;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
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
