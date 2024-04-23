package main.todolist.sceneController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.todolist.MainController;
import main.todolist.model.ToDoItem;
import main.todolist.DButils.DButils;

public class ToDoDetailController {

    private ToDoItem toDoItem;

    @FXML
    private Button button_save;

    @FXML
    private DatePicker date_from;

    @FXML
    private DatePicker date_to;

    @FXML
    private TextArea field_toDoDesc;

    @FXML
    private TextField field_toDoName;

    public void initialize(ToDoItem item) {
        toDoItem = item;
        // Populate fields with ToDoItem details
        field_toDoName.setText(item.getName());
        field_toDoDesc.setText(item.getDescription());
        date_from.setValue(item.getDateFrom());
        date_to.setValue(item.getDateTo());
    }

    @FXML
    void saveToDo(ActionEvent event) {
        // Update ToDoItem with edited details
        toDoItem.setName(field_toDoName.getText());
        toDoItem.setDescription(field_toDoDesc.getText());
        toDoItem.setDateFrom(date_from.getValue());
        toDoItem.setDateTo(date_to.getValue());

        // Save ToDoItem to the database

        // Close the window
        Stage stage = (Stage) button_save.getScene().getWindow();
        stage.close();
    }
}
