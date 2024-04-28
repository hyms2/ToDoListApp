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

import java.io.IOException;

public class SignUpController {
    @FXML
    public Button button_login;

    @FXML
    private Button button_signup;

    @FXML
    private PasswordField field_password;

    @FXML
    private TextField field_username;

    @FXML
    private Label label_error;

    @FXML
    void pressSignUp(ActionEvent event) {
        String username = field_username.getText().trim();
        String password = field_password.getText().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            label_error.setText("");
            DButils.signUp(username, password);
        } else {
            label_error.setText("Please fill all information");
        }
    }

    @FXML
    void pressLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Parent root = fxmlLoader.load();

        Stage oldStage = (Stage) button_login.getScene().getWindow();
        oldStage.close();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void usernameTaken() {
        label_error.setText("Username is taken. Try another username");
    }
}
