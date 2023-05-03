module com.uid.progettobanca {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.uid.progettobanca to javafx.fxml;
    exports com.uid.progettobanca;
}