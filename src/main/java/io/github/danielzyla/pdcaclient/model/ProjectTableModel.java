package io.github.danielzyla.pdcaclient.model;

import io.github.danielzyla.pdcaclient.dto.ProjectReadDto;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectTableModel {
    private final SimpleLongProperty id;
    private final SimpleObjectProperty<LocalDateTime> startTime;
    private final SimpleStringProperty projectName;
    private final SimpleStringProperty projectCode;
    private final SimpleObjectProperty<List<Department>> departments;
    private final SimpleObjectProperty<List<Product>> products;
    private final SimpleObjectProperty<List<Cycle>> cycles;
    private final SimpleBooleanProperty complete;
    private final SimpleObjectProperty<LocalDateTime> endTime;

    public ProjectTableModel(ProjectReadDto projectReadDto) {
        this.id = new SimpleLongProperty(projectReadDto.getId());
        this.startTime = new SimpleObjectProperty<>(projectReadDto.getStartTime());
        this.projectName = new SimpleStringProperty(projectReadDto.getProjectName());
        this.projectCode = new SimpleStringProperty(projectReadDto.getProjectCode());
        this.departments = new SimpleObjectProperty<>(projectReadDto.getDepartments());
        this.products = new SimpleObjectProperty<>(projectReadDto.getProducts());
        this.cycles = new SimpleObjectProperty<>(projectReadDto.getCycles());
        this.complete = new SimpleBooleanProperty(projectReadDto.isComplete());
        this.endTime = new SimpleObjectProperty<>(projectReadDto.getEndTime());
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

    void setStartTime(final LocalDateTime startTime) {
        this.startTime.set(startTime);
    }

    public String getProjectName() {
        return projectName.get();
    }

    public SimpleStringProperty projectNameProperty() {
        return projectName;
    }

    void setProjectName(final String projectName) {
        this.projectName.set(projectName);
    }

    public String getProjectCode() {
        return projectCode.get();
    }

    public SimpleStringProperty projectCodeProperty() {
        return projectCode;
    }

    void setProjectCode(final String projectCode) {
        this.projectCode.set(projectCode);
    }

    public boolean isComplete() {
        return complete.get();
    }

    public SimpleBooleanProperty completeProperty() {
        return complete;
    }

    void setComplete(final boolean complete) {
        this.complete.set(complete);
    }

    public LocalDateTime getEndTime() {
        return endTime.get();
    }

    public SimpleObjectProperty<LocalDateTime> endTimeProperty() {
        return endTime;
    }

    void setEndTime(final LocalDateTime endTime) {
        this.endTime.set(endTime);
    }

    public List<Department> getDepartments() {
        return departments.get();
    }

    public SimpleObjectProperty<List<Department>> departmentsProperty() {
        return departments;
    }

    void setDepartments(final List<Department> departments) {
        this.departments.set(departments);
    }

    public List<Product> getProducts() {
        return products.get();
    }

    public SimpleObjectProperty<List<Product>> productsProperty() {
        return products;
    }

    void setProducts(final List<Product> products) {
        this.products.set(products);
    }

    public List<Cycle> getCycles() {
        return cycles.get();
    }

    public SimpleObjectProperty<List<Cycle>> cyclesProperty() {
        return cycles;
    }

    void setCycles(final List<Cycle> cycles) {
        this.cycles.set(cycles);
    }
}

