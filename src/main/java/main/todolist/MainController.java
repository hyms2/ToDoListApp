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
import main.todolist.model.UserInfo;
import main.todolist.sceneController.AddToDoController;
import main.todolist.sceneController.ToDoDetailController;

import java.io.IOException;
import java.util.List;

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
    private Button button_logout;
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
            stage.setTitle("Add New Quest");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            ToDoItem newItem = addToDoController.getNewItem();
            if (newItem != null) {
                receiveToDoItem(newItem);
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
        ToDoItem selectedItem = listview_ToDoList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("toDoDetail.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                ToDoDetailController detailController = fxmlLoader.getController();
                detailController.initialize(selectedItem);
                detailController.setMainController(this);

                Stage stage = new Stage();
                stage.setTitle("Quest Detail");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No ToDoItem Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a ToDoItem to view details.");
            alert.showAndWait();
        }
    }

    @FXML
    public void removeToDoBuilder(ActionEvent event) {
        Tab selectedTab = tab_ongoing.isSelected() ? tab_ongoing : tab_completed;

        if (selectedTab == tab_ongoing) {
            ToDoItem selectedItem = listview_ToDoList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                listview_ToDoList.getItems().remove(selectedItem);
                DButils.deleteToDoItem(selectedItem);
            }
        } else if (selectedTab == tab_completed) {
            ToDoItem selectedItem = listview_TDCompleted.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                listview_TDCompleted.getItems().remove(selectedItem);
                DButils.deleteToDoItem(selectedItem);
            }
        }
    }

    @FXML
    void pressLogout(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Parent root = fxmlLoader.load();

        Stage oldStage = (Stage) button_logout.getScene().getWindow();
        oldStage.close();

        Stage stage = new Stage();
        stage.setTitle("Login");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void initialize() {
        List<ToDoItem> ongoingToDoItems = DButils.getIncompleteToDoItems();
        List<ToDoItem> completeToDoItems = DButils.getCompleteToDoItems();
        listview_ToDoList.getItems().addAll(ongoingToDoItems);
        listview_TDCompleted.getItems().addAll(completeToDoItems);

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
                                    DButils.completeStatus(item.getId(), 1);
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
                                    listview_ToDoList.getItems().add(item);
                                    DButils.completeStatus(item.getId(), 0);
                                    listview_TDCompleted.getItems().remove(item);
                                }
                            });
                        }
                    }
                };
            }
        });
    }
    public void refreshToDoList() {
        listview_ToDoList.getItems().clear();
        listview_ToDoList.getItems().addAll(DButils.getIncompleteToDoItems());
    }
}
