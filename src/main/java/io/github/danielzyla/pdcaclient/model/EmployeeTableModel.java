package io.github.danielzyla.pdcaclient.model;

import io.github.danielzyla.pdcaclient.dto.EmployeeReadDto;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class EmployeeTableModel {
    private final SimpleLongProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty surname;
    private final SimpleStringProperty email;
    private final SimpleObjectProperty<Department> department;

    public EmployeeTableModel(EmployeeReadDto employeeReadDto) {
        this.id = new SimpleLongProperty(employeeReadDto.getId());
        this.name = new SimpleStringProperty(employeeReadDto.getName());
        this.surname = new SimpleStringProperty(employeeReadDto.getSurname());
        this.email = new SimpleStringProperty(employeeReadDto.getEmail());
        this.department = new SimpleObjectProperty<>(employeeReadDto.getDepartment());
    }

    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public Department getDepartment() {
        return department.get();
    }

    public SimpleObjectProperty<Department> departmentProperty() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department.set(department);
    }
}
