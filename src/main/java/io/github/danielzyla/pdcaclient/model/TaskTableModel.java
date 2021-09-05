package io.github.danielzyla.pdcaclient.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskTableModel {
    private final SimpleLongProperty id;
    private final SimpleObjectProperty<LocalDateTime> startTime;
    private final SimpleStringProperty description;
    private final SimpleObjectProperty<List<Employee>> employees;
    private final SimpleObjectProperty<LocalDate> deadline;
    private final SimpleStringProperty taskStatus;
    private final SimpleBooleanProperty complete;
    private final SimpleObjectProperty<LocalDateTime> executionTime;
    private final String taskClass;

    public TaskTableModel(Task source) {
        this.id = new SimpleLongProperty(source.getId());
        this.startTime = new SimpleObjectProperty<>(source.getStartTime());
        this.description = new SimpleStringProperty(source.getDescription());
        this.employees = new SimpleObjectProperty<>(new ArrayList<>(source.getEmployees()));
        this.deadline = new SimpleObjectProperty<>(source.getDeadline());
        this.taskStatus = new SimpleStringProperty(source.getTaskStatus());
        this.complete = new SimpleBooleanProperty(source.isComplete());
        this.executionTime = new SimpleObjectProperty<>(source.getExecutionTime());
        this.taskClass = source.getClass().getSimpleName();
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

    public LocalDateTime getStartTime() {
        return startTime.get();
    }

    public SimpleObjectProperty<LocalDateTime> startTimeProperty() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime.set(startTime);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public List<Employee> getEmployees() {
        return employees.get();
    }

    public SimpleObjectProperty<List<Employee>> employeesProperty() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees.set(employees);
    }

    public LocalDate getDeadline() {
        return deadline.get();
    }

    public SimpleObjectProperty<LocalDate> deadlineProperty() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline.set(deadline);
    }

    public String getTaskStatus() {
        return taskStatus.get();
    }

    public SimpleStringProperty taskStatusProperty() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus.set(taskStatus);
    }

    public boolean isComplete() {
        return complete.get();
    }

    public SimpleBooleanProperty completeProperty() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete.set(complete);
    }

    public LocalDateTime getExecutionTime() {
        return executionTime.get();
    }

    public SimpleObjectProperty<LocalDateTime> executionTimeProperty() {
        return executionTime;
    }

    public void setExecutionTime(LocalDateTime executionTime) {
        this.executionTime.set(executionTime);
    }

    public String getTaskClass() {
        return taskClass;
    }
}
