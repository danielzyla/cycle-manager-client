package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.Employee;

import java.time.LocalDateTime;
import java.util.List;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getCurrentSituationAnalysis() {
        return currentSituationAnalysis;
    }

    public void setCurrentSituationAnalysis(String currentSituationAnalysis) {
        this.currentSituationAnalysis = currentSituationAnalysis;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getRootCauseIdentification() {
        return rootCauseIdentification;
    }

    public void setRootCauseIdentification(String rootCauseIdentification) {
        this.rootCauseIdentification = rootCauseIdentification;
    }

    public String getOptimalSolutionChoice() {
        return optimalSolutionChoice;
    }

    public void setOptimalSolutionChoice(String optimalSolutionChoice) {
        this.optimalSolutionChoice = optimalSolutionChoice;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
