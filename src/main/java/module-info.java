module com.example.productosappmvc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.productosappmvc to javafx.fxml;
    exports com.example.productosappmvc;
}