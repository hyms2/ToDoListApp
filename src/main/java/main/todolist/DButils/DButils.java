package main.todolist.DButils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import main.todolist.Main;
import main.todolist.login.LoginController;
import main.todolist.login.SignUpController;
import main.todolist.model.ToDoItem;
import main.todolist.model.UserInfo;

import java.io.IOException;
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

    private static void closeConnection(Connection conn, Statement statement, ResultSet resultSet) {
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

    public static Integer insertToDoItem(ToDoItem item) {
        Integer generatedId = -1;

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("INSERT INTO todo_detail (name, description, date_from, date_to, iscomplete, user_id) VALUES (?, ?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            statement.setDate(3, Date.valueOf(item.getDateFrom()));
            statement.setDate(4, Date.valueOf(item.getDateTo()));
            statement.setInt(5, item.isCompleted() ? 1 : 0);
            statement.setInt(6, loggedInUser());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Inserting todo item failed, no rows affected.");
            }

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedId = resultSet.getInt(1);
                    item.setId(generatedId);
                } else {
                    throw new SQLException("Inserting todo item failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(null, null, null);
        }

        return generatedId;
    }

    public static void deleteToDoItem(ToDoItem item) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("DELETE FROM todo_detail WHERE id=? AND user_id=" +loggedInUser())) {
            statement.setInt(1, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(null, null, null);
        }
    }

    public static void completeStatus(Integer itemId, int isComplete) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("UPDATE todo_detail SET iscomplete = ? WHERE id = ?")) {
            statement.setInt(1, isComplete);
            statement.setInt(2, itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(null, null, null);
        }
    }

    private static List<ToDoItem> getToDoItems(String query) {
        List<ToDoItem> toDoItems = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                LocalDate dateFrom = resultSet.getDate("date_from").toLocalDate();
                LocalDate dateTo = resultSet.getDate("date_to").toLocalDate();
                Integer userId = resultSet.getInt("user_id");

                ToDoItem item = new ToDoItem(name, description, dateFrom, dateTo, userId);
                item.setId(id);

                toDoItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(null, null, null);
        }
        return toDoItems;
    }

    public static List<ToDoItem> getIncompleteToDoItems() {
        return getToDoItems("SELECT * FROM todo_detail WHERE iscomplete = 0 AND user_id =" + loggedInUser());
    }

    public static List<ToDoItem> getCompleteToDoItems() {
        return getToDoItems("SELECT * FROM todo_detail WHERE iscomplete = 1 AND user_id =" + loggedInUser());
    }

    public static void updateToDoItem(ToDoItem item) {
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("UPDATE todo_detail SET name=?, description=?, date_from=?, date_to=? WHERE id=?")) {
            statement.setString(1, item.getName());
            statement.setString(2, item.getDescription());
            statement.setDate(3, Date.valueOf(item.getDateFrom()));
            statement.setDate(4, Date.valueOf(item.getDateTo()));
            statement.setInt(5, item.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating todo item failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(null, null, null);
        }
    }

    public static void signUp(String username, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            if (username.length() < 5) {
                System.out.println("Username must be at least 5 characters long.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("INVALID USERNAME");
                alert.setContentText("Username must be at least 5 characters long.");
                alert.show();
                return;
            }
            if (password.length() < 8) {
                System.out.println("Password must be at least 8 characters long.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("INVALID PASSWORD");
                alert.setContentText("Password must be at least 8 characters long.");
                alert.show();
                return;
            }
            if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).*$")) {
                System.out.println("Password must contain at least an uppercase, a lowercase,and a number.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("INVALID PASSWORD");
                alert.setContentText("Password must contain at least an uppercase, a lowercase, and a number.");
                alert.show();
                return;
            }
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("signup.fxml"));
            Parent root = loader.load();

            SignUpController signUpController = loader.getController();

            if (resultSet.next()) {
                System.out.println("Username already taken!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("USERNAME ALREADY TAKEN!");
                alert.setContentText("Try insert another username.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users(username, password) VALUES (?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sign Up successful");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(connection, psCheckUserExists, psInsert, resultSet);
        }
    }

    public static void logIn(String username, String password, LoginController loginController) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Stage loginStage = (Stage) loginController.button_login.getScene().getWindow();

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("login.fxml"));
            Parent root = loader.load();

            LoginController loginController1 =loader.getController();

            if (!resultSet.next()) {
                System.out.println("Wrong username");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username or password is wrong");
                alert.show();
            } else {
                String fetchPassword = resultSet.getString("password");
                if (fetchPassword.equals(password)) {
                    System.out.println("Login successful");



                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main2.fxml"));
                    Parent root1 = fxmlLoader.load();

                    loginStage.close();

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));
                    stage.show();
                } else {
                    System.out.println("Wrong password");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Username or password is wrong");
                    alert.show();
                    loginController1.label_error.setText("Username or password is incorrect!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeResources(connection, preparedStatement, preparedStatement, resultSet);
        }
    }

    private static void closeResources(Connection connection, PreparedStatement psCheckUserExists, PreparedStatement psInsert, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (psCheckUserExists != null) {
                psCheckUserExists.close();
            }
            if (psInsert != null) {
                psInsert.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getUserId(String username) {
        int userId = -1;

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT id_user FROM users WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("id_user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userId;
    }

    public static int loggedInUser(){
        String username = UserInfo.getUsername();
        Integer userId = Integer.valueOf(getUserId(username));
        return userId;
    }
}