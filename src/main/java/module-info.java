module project.password_generator {
    requires javafx.controls;
    requires javafx.fxml;


    opens project.password_generator to javafx.fxml;
    exports project.password_generator;
}