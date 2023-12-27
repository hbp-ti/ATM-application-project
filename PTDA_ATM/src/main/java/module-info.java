module PTDA_ATM {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.mail;

    opens PTDA_ATM to javafx.fxml;
    exports PTDA_ATM;
    exports SQL;
    opens SQL to javafx.fxml;
}