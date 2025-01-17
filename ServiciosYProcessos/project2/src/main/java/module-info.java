module com.project2.project2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires jbcrypt;

    opens com.project2.project2 to javafx.fxml;
    exports com.project2.project2;
    exports com.project2.project2.Controllers;
    opens com.project2.project2.Controllers to javafx.fxml;
    exports com.project2.project2.MessageLogic;
    opens com.project2.project2.MessageLogic to javafx.fxml;
    exports com.project2.project2.Client;
    opens com.project2.project2.Client to javafx.fxml;
    exports com.project2.project2.Server;
    opens com.project2.project2.Server to javafx.fxml;
}