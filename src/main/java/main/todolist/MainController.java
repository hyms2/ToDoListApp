package main.todolist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.todolist.DButils.DButils;
import main.todolist.model.ToDoItem;
import java.time.format.DateTimeFormatter;
import javafx.util.Callback;
import main.todolist.sceneController.AddToDoController;
import main.todolist.sceneController.ToDoDetailController;

import java.io.IOException;

public class MainController {

    @FXML
    private Button button_addToDo;

    @FXML
    private ListView<ToDoItem> listview_ToDoList;

    @FXML
    private Tab tab_completed;

    @FXML
    private Tab tab_ongoing;

    @FXML
    private Button button_detailToDo;

    @FXML
    private ListView<ToDoItem> listview_TDCompleted;

    @FXML
    void openToDoBuilder(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addToDo.fxml"));
            Parent root = (Parent) fxmlLoader.load();

            AddToDoController addToDoController = fxmlLoader.getController();
            addToDoController.setMainController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Show the dialog and wait for it to close

            // After the dialog is closed, retrieve the new item from AddToDoController
            ToDoItem newItem = addToDoController.getNewItem();
            if (newItem != null) {
                receiveToDoItem(newItem); // Add the new item to the list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void receiveToDoItem(ToDoItem item) {
        listview_ToDoList.getItems().add(item);
    }

    @FXML
    void openToDoDetail(ActionEvent event) {
        // Get the selected ToDoItem
        ToDoItem selectedItem = listview_ToDoList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                // Load the ToDoDetail pop-up window FXML
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("todoDetail.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                // Get the controller
                ToDoDetailController detailController = fxmlLoader.getController();

                // Pass the selected ToDoItem and the listview_ToDoList to the controller
                detailController.initialize(selectedItem);

                // Create a new stage for the pop-up window
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                // Set the pop-up window as modal (blocks interaction with other windows)
                stage.initModality(Modality.APPLICATION_MODAL);

                // Show the pop-up window
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Show an alert if no ToDoItem is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No ToDoItem Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a ToDoItem to view details.");
            alert.showAndWait();
        }
    }

    @FXML
    public void removeToDoBuilder(ActionEvent event) {
        // Get the currently selected tab
        Tab selectedTab = tab_ongoing.isSelected() ? tab_ongoing : tab_completed;

        // Check which list view corresponds to the selected tab and remove the selected item
        if (selectedTab == tab_ongoing) {
            ToDoItem selectedItem = listview_ToDoList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                listview_ToDoList.getItems().remove(selectedItem);

                // Remove the item from the database
                DButils.deleteToDoItem(selectedItem);
            }
        } else if (selectedTab == tab_completed) {
            ToDoItem selectedItem = listview_TDCompleted.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                listview_TDCompleted.getItems().remove(selectedItem);

                // Remove the item from the database
                DButils.deleteToDoItem(selectedItem);
            }
        }
    }



    public void initialize() {
        listview_ToDoList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> param) {
                return new ListCell<>() {
                    private CheckBox checkBox = new CheckBox();

                    @Override
                    protected void updateItem(ToDoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getName() + " - From: " + item.getDateFrom().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                                    " To: " + item.getDateTo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            setGraphic(checkBox);
                            checkBox.setSelected(false);
                            checkBox.setOnAction(event -> {
                                if (checkBox.isSelected()) {
                                    listview_TDCompleted.getItems().add(item);
                                    listview_ToDoList.getItems().remove(item);
                                }
                            });
                        }
                    }
                };
            }
        });

        listview_TDCompleted.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> param) {
                return new ListCell<>() {
                    private CheckBox checkBox = new CheckBox();

                    @Override
                    protected void updateItem(ToDoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getName() + " - From: " + item.getDateFrom().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                                    " To: " + item.getDateTo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            setGraphic(checkBox);
                            checkBox.setSelected(true);
                            checkBox.setOnAction(event -> {
                                if (!checkBox.isSelected()) {
                                    // Move the item to the ongoing tab
                                    listview_ToDoList.getItems().add(item);
                                    listview_TDCompleted.getItems().remove(item);
                                }
                            });
                        }
                    }
                };
            }
        });
    }

    public void removeToDoItem(ToDoItem item) {
        listview_ToDoList.getItems().remove(item);
        listview_TDCompleted.getItems().remove(item);
        // Optionally, remove the item from the database
        DButils.deleteToDoItem(item);
    }

}
