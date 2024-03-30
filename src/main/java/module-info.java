module org.example.indiecure {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires java.sql;


    opens org.example.indiecure to javafx.fxml;
    exports org.example.indiecure;
}