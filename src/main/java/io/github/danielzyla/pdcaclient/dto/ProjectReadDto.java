package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.Cycle;
import io.github.danielzyla.pdcaclient.model.Department;
import io.github.danielzyla.pdcaclient.model.Product;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectReadDto {
    private long id;
    private LocalDateTime startTime;
    private String projectName;
    private String projectCode;
    private List<Department> departments;
    private List<Product> products;
    private List<Cycle> cycles;
    private LocalDateTime endTime;
    private boolean complete;
}
