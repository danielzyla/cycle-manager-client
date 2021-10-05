package io.github.danielzyla.pdcaclient.dto;

import io.github.danielzyla.pdcaclient.model.Cycle;
import io.github.danielzyla.pdcaclient.model.Department;
import io.github.danielzyla.pdcaclient.model.Product;

import java.time.LocalDateTime;
import java.util.List;

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Cycle> getCycles() {
        return cycles;
    }

    public void setCycles(List<Cycle> cycles) {
        this.cycles = cycles;
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
