package io.github.danielzyla.pdcaclient.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Department {
    private int id;
    private String deptName;
    private Set<Employee> employees;
    private Set<Project> projects = new HashSet<>();
}
