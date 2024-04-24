package main.todolist.sceneController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.todolist.DButils.DButils;
import main.todolist.MainController;
import main.todolist.model.ToDoItem;
import java.time.LocalDate;

public class AddToDoController {
    private MainController mainController;
    private ToDoItem newItem;

    @FXML
    private Button button_addToDo;

    @FXML
    private TextArea field_TDDescription;

    @FXML
    private TextField field_TDname;

    @FXML
    private DatePicker from_datePicker;

    @FXML
    private DatePicker to_datePicker;

    @FXML
    void addToDo(ActionEvent event) {
        String name = field_TDname.getText();
        String description = field_TDDescription.getText();
        LocalDate dateFrom = from_datePicker.getValue();
        LocalDate dateTo = to_datePicker.getValue();

        if (name.isEmpty() || description.isEmpty() || dateFrom == null || dateTo == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incomplete ToDo Information");
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
        } else {
            ToDoItem newItem = new ToDoItem(name, description, dateFrom, dateTo);
            DButils.insertToDoItem(newItem);
            mainController.receiveToDoItem(newItem);
            ((Stage) button_addToDo.getScene().getWindow()).close();
        }
    }

    public ToDoItem getNewItem() {
        return newItem;
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        from_datePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now()));
            }
        });

        to_datePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now()));
            }
        });
    }
}
