package main.todolist.DButils;

import main.todolist.model.ToDoItem;

import java.sql.*;

public class DButils {

    private static final String URL = "jdbc:mysql://localhost:3306/todolistdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "hazim1234";

    public static void insertToDoItem(ToDoItem item) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO todo_items (name, description, date_from, date_to) VALUES (?, ?, ?, ?)";
            if (item.getId() != null) {
                query = "UPDATE todo_items SET name=?, description=?, date_from=?, date_to=? WHERE id=?";
            }

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(item.getDateFrom()));
            statement.setDate(4, java.sql.Date.valueOf(item.getDateTo()));

            if (item.getId() != null) {
                statement.setLong(5, item.getId()); // Set ID for update
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteToDoItem(ToDoItem item) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "DELETE FROM todo_items WHERE name=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, item.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
