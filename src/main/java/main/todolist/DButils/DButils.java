package main.todolist.DButils;

import main.todolist.model.ToDoItem;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class DButils {
    private static final String URL = "jdbc:mysql://localhost:3306/todolistdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "hazim1234";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    private static void closeResources(Connection conn, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static long insertToDoItem(ToDoItem item) {
        long generatedId = -1;

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("INSERT INTO todo_items (name, description, date_from, date_to, iscomplete) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(item.getDateFrom()));
            statement.setDate(4, java.sql.Date.valueOf(item.getDateTo()));
            statement.setInt(5, item.isCompleted() ? 1 : 0);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserting todo item failed, no rows affected.");
            }

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedId = resultSet.getLong(1);
                } else {
                    throw new SQLException("Inserting todo item failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, null, null);
        }

        return generatedId;
    }

    public static void deleteToDoItem(ToDoItem item) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("DELETE FROM todo_items WHERE id=?")) {
            statement.setLong(1, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, null, null);
        }
    }

    public static void completeStatus(long itemId, int isComplete) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("UPDATE todo_items SET iscomplete = ? WHERE id = ?")) {
            statement.setInt(1, isComplete);
            statement.setLong(2, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, null, null);
        }
    }

    private static List<ToDoItem> getToDoItems(String query) {
        List<ToDoItem> toDoItems = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                LocalDate dateFrom = resultSet.getDate("date_from").toLocalDate();
                LocalDate dateTo = resultSet.getDate("date_to").toLocalDate();

                ToDoItem item = new ToDoItem(name, description, dateFrom, dateTo);
                item.setId(id);

                toDoItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, null, null);
        }

        return toDoItems;
    }

    public static List<ToDoItem> getIncompleteToDoItems() {
        return getToDoItems("SELECT * FROM todo_items WHERE iscomplete = 0");
    }

    public static List<ToDoItem> getCompleteToDoItems() {
        return getToDoItems("SELECT * FROM todo_items WHERE iscomplete = 1");
    }

    public static void updateToDoItem(ToDoItem item) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("UPDATE todo_items SET name=?, description=?, date_from=?, date_to=? WHERE id=?")) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(item.getDateFrom()));
            statement.setDate(4, java.sql.Date.valueOf(item.getDateTo()));
            statement.setLong(5, item.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating todo item failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, null, null);
        }
    }
}