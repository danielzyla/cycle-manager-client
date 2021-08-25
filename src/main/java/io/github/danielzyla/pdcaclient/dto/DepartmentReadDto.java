package io.github.danielzyla.pdcaclient.dto;

import lombok.Data;

@Data
public class DepartmentReadDto {
    private int id;
    private String deptName;

    @Override
    public String toString() {
        return deptName;
    }
}
