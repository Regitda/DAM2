module com.project1.project1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.project1.project1 to javafx.fxml;
    exports com.project1.project1;
}