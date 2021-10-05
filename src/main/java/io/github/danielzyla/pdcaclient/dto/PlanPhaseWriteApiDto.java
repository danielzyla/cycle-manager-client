package io.github.danielzyla.pdcaclient.dto;

import java.util.ArrayList;
import java.util.List;

public class PlanPhaseWriteApiDto {
    private long id;
    private String problemDescription,
            currentSituationAnalysis,
            goal,
            rootCauseIdentification,
            optimalSolutionChoice;
    private List<Long> employeeIds = new ArrayList<>();
    private boolean complete;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
