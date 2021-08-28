package io.github.danielzyla.pdcaclient.model;

import io.github.danielzyla.pdcaclient.dto.DepartmentReadDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DepartmentTableModel {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty deptName;

    public DepartmentTableModel(DepartmentReadDto departmentReadDto) {
        this.id = new SimpleIntegerProperty(departmentReadDto.getId());
        this.deptName = new SimpleStringProperty(departmentReadDto.getDeptName());
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getDeptName() {
        return deptName.get();
    }

    public SimpleStringProperty deptNameProperty() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName.set(deptName);
    }
}
