package io.github.danielzyla.pdcaclient.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectWriteApiDto {
    private Long id;
    private String projectName, projectCode;
    private List<Integer> departmentIds = new ArrayList<>();
    private List<Long> productIds = new ArrayList<>();
}