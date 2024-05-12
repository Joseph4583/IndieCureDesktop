module org.example.indiecure_aplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;

    opens org.example.indiecure_aplication to javafx.fxml;
    exports org.example.indiecure_aplication;
    opens org.example.indiecure_aplication.Controller to javafx.fxml;
    exports org.example.indiecure_aplication.Controller;
    opens org.example.indiecure_aplication.Model.Classes to javafx.fxml;
    exports org.example.indiecure_aplication.Model.Classes;
    opens org.example.indiecure_aplication.Model.Utils to javafx.fxml;
    exports org.example.indiecure_aplication.Model.Utils;
    opens org.example.indiecure_aplication.Model.Exceptions to javafx.fxml;
    exports org.example.indiecure_aplication.Model.Exceptions;
}