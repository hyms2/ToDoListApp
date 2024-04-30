module main.todolist {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;

    exports main.todolist;
    opens main.todolist to javafx.fxml;
    exports main.todolist.sceneController;
    opens main.todolist.sceneController to javafx.fxml;
    exports main.todolist.login;
    opens main.todolist.login to javafx.fxml;
}