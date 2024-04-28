package main.todolist.model;

import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ToDoItem {
    private Integer id;
    private String name;
    private String description;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private BooleanProperty completed;
    private Integer userId;

    public ToDoItem(String name, String description, LocalDate dateFrom, LocalDate dateTo, Integer userId) {
        this.name = name;
        this.description = description;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.completed = new SimpleBooleanProperty(false);
        this.userId = userId;
    }

    public Integer getId() {
        return id;
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
    public void setId(Integer id) {
        this.id = id;
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
    public Integer getUserId() {
        return userId;
    }

    public boolean isCompleted() {
        return completed.get();
    }
}
