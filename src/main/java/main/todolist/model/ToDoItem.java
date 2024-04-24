package main.todolist.model;

import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ToDoItem {
    private Long id; // Add an id field
    private String name;
    private String description;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private BooleanProperty completed;

    public ToDoItem(String name, String description, LocalDate dateFrom, LocalDate dateTo) {
        this.name = name;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.completed = new SimpleBooleanProperty(false);
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCompleted() {
        return completed.get();
    }
    public BooleanProperty completedProperty() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public LocalDate getDateFrom() {
        return dateFrom;
    }
    public LocalDate getDateTo() {
        return dateTo;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }
    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}
