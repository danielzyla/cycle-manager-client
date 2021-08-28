package io.github.danielzyla.pdcaclient.dto;

import lombok.Data;

@Data
public class EmployeeWriteApiDto {
    private long id;
    private String name, surname;
    private String email;
    private int departmentId;
}
