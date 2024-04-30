package main.todolist.sceneController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.todolist.DButils.DButils;
import main.todolist.MainController;
import main.todolist.model.ToDoItem;

import java.time.LocalDate;

public class TDDCompletedController {

    private ToDoItem toDoItem;
    private MainController mainController;
    @FXML
    private DatePicker date_from;
    @FXML
    private DatePicker date_to;
    @FXML
    private TextArea field_toDoDesc;
    @FXML
    private TextField field_toDoName;

    public void initialize() {
        field_toDoName.setEditable(false);
        field_toDoDesc.setEditable(false);
        date_from.setEditable(false);
        date_from.setOnMouseClicked(e -> {
            if(!date_from.isEditable())
                date_from.hide();
        });
        date_to.setEditable(false);
        date_to.setOnMouseClicked(e -> {
            if(!date_to.isEditable())
                date_to.hide();
        });
    }

    public void initialize(ToDoItem item) {
        toDoItem = item;
        field_toDoName.setText(item.getName());
        field_toDoDesc.setText(item.getDescription());
        date_from.setValue(item.getDateFrom());
        date_to.setValue(item.getDateTo());
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
