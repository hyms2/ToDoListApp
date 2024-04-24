package main.todolist.sceneController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    public void changeScene(String fxmlFileName, Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFileName));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
