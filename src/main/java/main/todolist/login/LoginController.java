package main.todolist.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.todolist.DButils.DButils;
import main.todolist.Main;
import main.todolist.model.UserInfo;

import java.io.IOException;

public class LoginController {
    @FXML
    public Button button_login;

    @FXML
    private Button button_signup;

    @FXML
    private PasswordField field_password;

    @FXML
    private TextField field_username;

    @FXML
    public Label label_error;

    @FXML
    void pressLogin(ActionEvent event) {
        String username = field_username.getText().trim();
        String password = field_password.getText().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            label_error.setText("");
            UserInfo loggedInUser = new UserInfo(username);
            loggedInUser.getUserId();
            DButils.logIn(username, password, this);
        } else {
            label_error.setText("Please fill in both username and password.");
        }
    }

    @FXML
    void presssignup(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("signup.fxml"));
        Parent root = fxmlLoader.load();

        Stage loginStage = (Stage) button_signup.getScene().getWindow();
        loginStage.close();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
