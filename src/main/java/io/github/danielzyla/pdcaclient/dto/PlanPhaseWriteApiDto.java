package io.github.danielzyla.pdcaclient.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PlanPhaseWriteApiDto {
    private long id;
    private String problemDescription,
            currentSituationAnalysis,
            goal,
            rootCauseIdentification,
            optimalSolutionChoice;
    private List<Long> employeeIds = new ArrayList<>();
    private boolean complete;
}
