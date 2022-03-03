module Luca {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;

    opens Luca to javafx.fxml;
    exports Luca;
}
