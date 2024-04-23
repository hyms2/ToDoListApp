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
    private BooleanProperty completed; // Define completed property

    public ToDoItem(String name, String description, LocalDate dateFrom, LocalDate dateTo) {
        this.name = name;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.completed = new SimpleBooleanProperty(false); // Initialize completed property
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and setter methods for completed property
    public boolean isCompleted() {
        return completed.get();
    }

    public BooleanProperty completedProperty() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed.set(completed);
    }

    // Getter methods for other fields
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

    // Method to check if the id is not null
    public boolean hasId() {
        return id != null;
    }
}
