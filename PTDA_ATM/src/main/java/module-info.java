module atm.ptda_atm {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens atm.ptda_atm to javafx.fxml;
    exports atm.ptda_atm;
}